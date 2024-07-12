# Define the Maven executable (customize if needed)
MAVEN = mvn clean

# Default target
all: compile

# Compile the project
compile:
	$(MAVEN) compile

# Run tests
test:
	$(MAVEN) test

# Package the project (create a JAR file)
package:
	$(MAVEN) package

# Install the project to the local repository
install:
	$(MAVEN) install

# Clean and build the project
rebuild: clean compile

# Run the project (assuming there is a main class specified)
run:
	$(MAVEN) exec:java -Dexec.mainClass="com.example.Main"

# Deploy the project (upload to remote repository)
deploy:
	$(MAVEN) deploy

# Display help
help:
	@echo "Available targets:"
	@echo "  all        - Compile the project"
	@echo "  compile    - Compile the project"
	@echo "  test       - Run tests"
	@echo "  package    - Package the project into a JAR file"
	@echo "  install    - Install the project to the local repository"
	@echo "  clean      - Clean the project (delete target directory)"
	@echo "  rebuild    - Clean and compile the project"
	@echo "  run        - Run the project (requires a main class)"
	@echo "  deploy     - Deploy the project to a remote repository"
	@echo "  help       - Display this help message"

# Phony targets (not actual files)
.PHONY: all compile test package install clean rebuild run deploy help
