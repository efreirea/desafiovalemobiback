SRC_DIR = ./src
BUILD_DIR = ./bin
LIB_DIR = ./lib
CPATH = $(LIB_DIR)/*:$(BUILD_DIR)

all: compile run

compile:
	@javac $(SRC_DIR)/database/*.java -cp $(CPATH) -d $(BUILD_DIR)
	@javac $(SRC_DIR)/Main.java  -cp $(CPATH) -d $(BUILD_DIR)
run:
	@java -cp $(CPATH) Main 
clean:
	rm -Rf $(BUILD_DIR)
	mkdir $(BUILD_DIR)
