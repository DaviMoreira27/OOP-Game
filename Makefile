SRC_DIR := src
BUILD_DIR := build
DIST_DIR := dist
MAIN_CLASS := Main
JAR_NAME := Game-name-here.jar

SOURCES := $(shell find $(SRC_DIR) -name "*.java")

.PHONY: build run export

build:
	mkdir -p $(BUILD_DIR)
	javac -d $(BUILD_DIR) -sourcepath $(SRC_DIR) $(SRC_DIR)/**/*.java

run: build
	java -cp $(BUILD_DIR) $(MAIN_CLASS)

export: build
	mkdir -p $(DIST_DIR)
	jar cfe $(DIST_DIR)/$(JAR_NAME) $(MAIN_CLASS) -C $(BUILD_DIR) .
