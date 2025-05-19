SRC_DIR := src
BUILD_DIR := build
DIST_DIR := dist
LIB_DIR := lib
MAIN_CLASS := Main
JAR_NAME := Game-name-here.jar
GSON_JAR := $(LIB_DIR)/gson-2.13.1.jar

SOURCES := $(shell find $(SRC_DIR) -name "*.java")

.PHONY: build run export clean

build:
	mkdir -p $(BUILD_DIR)
	javac -cp $(GSON_JAR) -d $(BUILD_DIR) -sourcepath $(SRC_DIR) $(SOURCES)

run: build
	java -cp "$(BUILD_DIR):$(GSON_JAR)" $(MAIN_CLASS)

export: build
	mkdir -p $(DIST_DIR)
	jar cfe $(DIST_DIR)/$(JAR_NAME) $(MAIN_CLASS) -C $(BUILD_DIR) .

clean:
	rm -rf $(BUILD_DIR) $(DIST_DIR)
