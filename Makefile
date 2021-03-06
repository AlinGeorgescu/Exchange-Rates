JFLAGS = -g
JC = javac
JVM = java

all: build

sources = $(wildcard *.java)
classes = $(sources:.java=.class)

build: $(classes)

run: build
	$(JVM) Main $(arg)

.PHONY: clean
clean :
	rm -f *.class *~ tmp_rates.xml
%.class : %.java
	$(JC) $(JFLAGS) $<
