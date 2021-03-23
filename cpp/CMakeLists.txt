cmake_minimum_required(VERSION 3.0)

project(PokerHandEvaluator VERSION 0.3.1)

set(CMAKE_BUILD_TYPE  "Release")

# Use C++ 11 Standard to compile
set(CMAKE_CXX_STANDARD 11)

set(CMAKE_C_STANDARD 99)

# Download and unpack googletest at configure time
configure_file(CMakeLists.txt.in googletest-download/CMakeLists.txt)
execute_process(COMMAND ${CMAKE_COMMAND} -G "${CMAKE_GENERATOR}" .
  RESULT_VARIABLE result
  WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}/googletest-download )
if(result)
  message(FATAL_ERROR "CMake step for googletest failed: ${result}")
endif()
execute_process(COMMAND ${CMAKE_COMMAND} --build .
  RESULT_VARIABLE result
  WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}/googletest-download )
if(result)
  message(FATAL_ERROR "Build step for googletest failed: ${result}")
endif()

# Prevent overriding the parent project's compiler/linker
# settings on Windows
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)

# Add googletest directly to our build. This defines
# the gtest and gtest_main targets.
add_subdirectory(${CMAKE_CURRENT_BINARY_DIR}/googletest-src
                 ${CMAKE_CURRENT_BINARY_DIR}/googletest-build
                 EXCLUDE_FROM_ALL)

configure_file(CMakeLists.txt.in googlebenchmark-download/CMakeLists.txt)
# executing the configuration step
execute_process(
    COMMAND ${CMAKE_COMMAND} -G "${CMAKE_GENERATOR}" .
    RESULT_VARIABLE results
    WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}/googlebenchmark-download
)
# checking if the configuration step passed
if(results)
    message(FATAL_ERROR "Configuration step for Benchmark has Failed. ${results}")
endif()
# executing the build step
execute_process(
    COMMAND ${CMAKE_COMMAND} --build . --config Release
    RESULT_VARIABLE results
    WORKING_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}/googlebenchmark-download
)
# checking if the build step passed
if(results)
    message(FATAL_ERROR "Build step for Benchmark has Failed. ${results}")
endif()
add_subdirectory(
    ${CMAKE_CURRENT_BINARY_DIR}/googlebenchmark-src
    ${CMAKE_CURRENT_BINARY_DIR}/googlebenchmark-build
    EXCLUDE_FROM_ALL
)

# Add the PokerHandEvaluator library
add_library(pheval STATIC
  src/dptables.c
  src/main.phevaluator.cc
  src/evaluator5.c
  src/evaluator6.c
  src/evaluator7.c
  src/hash.c
  src/hand.cc
  src/hashtable.c
  src/hashtable5.c
  src/hashtable6.c
  src/hashtable7.c
  src/rank.c
  src/7462.c
)
target_include_directories(pheval PUBLIC include)
target_compile_options(pheval PUBLIC -O3)

add_library(phevalomaha STATIC
  src/dptables.c
  src/tables_omaha.c
  src/evaluator_omaha.c
  src/evaluator_omaha.cc
  src/hash.c
  src/hand.cc
  src/hashtable.c
  src/rank.c
  src/7462.c
)
target_include_directories(phevalomaha PUBLIC include)
target_compile_options(phevalomaha PUBLIC -O3)

add_executable(unit_tests
  test/evaluation.cc
  test/evaluation_omaha.cc
  test/rank.cc
  test/kev/fast_eval.c
  test/kev/kev_eval.c
)
target_compile_options(unit_tests PUBLIC -O3)
target_link_libraries(unit_tests gtest_main pheval phevalomaha)

add_test(NAME unit_tests COMMAND unit_tests)

add_executable(benchmark_phevaluator
  main.benchmark/main.benchmark.cc
)
target_compile_options(benchmark_phevaluator PUBLIC -O3)
target_link_libraries(benchmark_phevaluator main.benchmark pheval phevalomaha)

add_executable(c_example
  examples/c_example.c
)
target_link_libraries(c_example pheval)

add_executable(cpp_example
  examples/cpp_example.cc
)
target_link_libraries(cpp_example pheval)

add_executable(omaha_example
  examples/omaha_example.cc
)
target_link_libraries(omaha_example phevalomaha)
