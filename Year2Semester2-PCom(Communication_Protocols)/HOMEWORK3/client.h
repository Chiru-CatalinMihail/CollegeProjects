#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include "parson.h"
#include "helpers.h"
#include "buffer.h"


#define MAXINPUT 14 //maximul este 13 pentru "enter_library"
#define BUFLEN 4096

#define HOST_SERVER "ec2-3-8-116-10.eu-west-2.compute.amazonaws.com"
#define IP_SERVER "3.8.116.10" //IP-UL LUI "ec2-3-8-116-10.eu-west-2.compute.amazonaws.com" gasit cu traceroute
#define PORT_SERVER 8080

/*
 * Macro de verificare a erorilor
 * Exemplu:
 *     int fd = open(file_name, O_RDONLY);
 *     DIE(fd == -1, "open failed");
 */

#define DIE(assertion, call_description)	\
	do {									\
		if (assertion) {					\
			fprintf(stderr, "(%s, %d): ",	\
					__FILE__, __LINE__);	\
			perror(call_description);		\
			exit(EXIT_FAILURE);				\
		}									\
	} while(0)

#define WARNING(assertion, call_description)\
	do {									\
		if (assertion) {					\
			fprintf(stderr, "(%s, %d): ",	\
					__FILE__, __LINE__);	\
			perror(call_description);		\
		}									\
	} while(0)


//functia de inregistrare la server a unui user nou
void reg();
//functia de login la server a unui user
void login(char* cookie, int* logged_in);
//functia de acces in biblioteca
void enter_library(char* cookie, char* token, int* access);
//functia care afiseaza toate cartile pe care le detine serverul
void get_books(char* cookie, char* token, int* access);
//functia care afiseaza o carte din biblioteca serverului pe baza unui id
//dat de client de la tastatura
void get_book(char* cookie, char* token, int* access);
//functia de adaugare a unei carti in biblioteca
void add_book(char* cookie, char* token, int* access);
//functia de stergere a unei carti din biblioteca
void delete_book(char* cookie, char* token, int* access);

//functia prin care un client se deconecteaza de la server
void logout(char* cookie, char* token, int* logged_in);


// computes and returns a POST request string (cookies can be NULL if not needed)
char *compute_post_request(char *ip_host, char *url, char* content_type, char* data,
            int content_length, char* cookie, char* JWT_token);


// computes and returns a GET request string (query_params
// and cookies can be set to NULL if not needed)
char *compute_get_request(char *host, char *url, char *query_params,
                          char *cookie, char* JWT_token, int type);
