cmake_minimum_required(VERSION 3.10)
project(reverse_parking LANGUAGES CXX C)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

find_package(PkgConfig REQUIRED)
pkg_check_modules(GTKMM REQUIRED gtkmm-3.0)
pkg_check_modules(GPIOD REQUIRED libgpiod)

add_executable(reverse-parking main.cpp)

target_include_directories(reverse-parking PRIVATE
    ${GTKMM_INCLUDE_DIRS} ${GPIOD_INCLUDE_DIRS})

target_link_libraries(reverse-parking
    ${GTKMM_LIBRARIES} ${GPIOD_LIBRARIES} pthread)

target_compile_options(reverse-parking PRIVATE
    ${GTKMM_CFLAGS_OTHER} ${GPIOD_CFLAGS_OTHER})
