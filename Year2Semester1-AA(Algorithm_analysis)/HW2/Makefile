JFLAGS = -g
JC = javac
JVM = java
RM = rm
W = -Xlint:all
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

MAIN = Main

default: build run clean

build:
	mkdir classes;
	$(JC) $(W) src/*.java -d classes;

run:
	$(JVM) -cp classes Main

clean:
	$(RM) -rf classes