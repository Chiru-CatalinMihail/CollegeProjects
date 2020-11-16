CC=gcc
CFLAGS = -Wall -g

all: client

# Compileaza client.c
client: client.c client_functions.c parson.c helpers.c buffer.c 
	$(CC) -o client client.c client_functions.c parson.c helpers.c buffer.c  $(CFLAGS)

.PHONY: clean run

# Ruleaza clientul
run: client
	./client

clean:
	rm -f client