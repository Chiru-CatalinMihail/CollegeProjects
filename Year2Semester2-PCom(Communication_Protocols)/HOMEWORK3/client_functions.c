#include "client.h"

void reg() {

	int sockfd = open_connection(IP_SERVER, PORT_SERVER, AF_INET, SOCK_STREAM, 0);
	char user[40], password[40];
	printf("username: ");
	scanf("%s", user);
	printf("password: ");
	scanf("%s", password);
	printf("\n");

	JSON_Value *root_val = json_value_init_object();
	JSON_Object *root_obj = json_value_get_object(root_val);

	json_object_set_string(root_obj, "username", user);
	json_object_set_string(root_obj, "password", password);

	char* msg = compute_post_request(IP_SERVER, "/api/v1/tema/auth/register",
	      "application/json", json_serialize_to_string_pretty(root_val),
	       strlen(json_serialize_to_string_pretty(root_val)), NULL, NULL);

	send_to_server(sockfd, msg);

	char* response = receive_from_server(sockfd);
	printf("%s\n", response);
	json_value_free(root_val);
	close(sockfd);
}

void login(char* cookie, int* logged_in) {

	int sockfd = open_connection(IP_SERVER, PORT_SERVER, AF_INET, SOCK_STREAM, 0);
	char user[40], password[40];
	printf("username: ");
	scanf("%s", user);
	printf("password: ");
	scanf("%s", password);

	JSON_Value *root_val = json_value_init_object();
	JSON_Object *root_obj = json_value_get_object(root_val);

	json_object_set_string(root_obj, "username", user);
	json_object_set_string(root_obj, "password", password);

	char* msg = compute_post_request(IP_SERVER, "/api/v1/tema/auth/login",
	        "application/json", json_serialize_to_string_pretty(root_val),
			strlen(json_serialize_to_string_pretty(root_val)), cookie, NULL);

	send_to_server(sockfd, msg);
	char* response = receive_from_server(sockfd);
	char* response_copy = calloc(strlen(response) + 1, sizeof(char));
	WARNING(response_copy == NULL, "Nu se mai poate aloca memorie pentru copia raspunsului");

	strcpy(response_copy, response);

	if (strstr(response_copy, "HTTP/1.1 200 OK")) {
		(*logged_in) = 1;
		char* aux = strstr(response_copy, "Set-Cookie:");
		strtok(aux, " \n");
		strcpy(cookie, strtok(NULL, " \n;"));
		strcat(cookie, "; ");
		strcat(cookie, strtok(NULL, " \n;"));
	}
	printf("%s\n", response);
	free(response_copy);
	json_value_free(root_val);
	close(sockfd);
}

void enter_library(char* cookie, char* token, int* access) {
	int sockfd = open_connection(IP_SERVER, PORT_SERVER, AF_INET, SOCK_STREAM, 0);

	char* msg = compute_get_request(IP_SERVER, "/api/v1/tema/library/access",
	                                 "application/json", cookie, NULL, 1);

	send_to_server(sockfd, msg);
	char* response = receive_from_server(sockfd);
	char* response_copy = calloc(strlen(response) + 1, sizeof(char));
	WARNING(response_copy == NULL, "Nu se mai poate aloca memorie pentru copia raspunsului");
	strcpy(response_copy, response);

	if (strstr(response_copy, "HTTP/1.1 200 OK")) {
		(*access) = 1;
		char* aux = strstr(response_copy, "token");
		strtok(aux, " \"");
		aux = strtok(NULL, "\"");
		strcpy(token, strtok(NULL, " \n;"));
		token[strlen(token)-2] = 0;	//nu ne intereseaza ultimele 2 caractere
									// ""}"
	} else {
		(*access) = 0;
	}
	printf("%s\n\n", response);
	free(response_copy);
	close(sockfd);
}

void get_books(char* cookie, char* token, int* access) {
	int sockfd = open_connection(IP_SERVER, PORT_SERVER, AF_INET, SOCK_STREAM, 0);

	char* msg = compute_get_request(IP_SERVER, "/api/v1/tema/library/books",
	                                 "application/json", cookie, token, 1);

	send_to_server(sockfd, msg);

	char* response = receive_from_server(sockfd);
	char* response_copy = calloc(strlen(response) + 1, sizeof(char));
	WARNING(response_copy == NULL, "Nu se mai poate aloca memorie pentru copia raspunsului");
	strcpy(response_copy, response);

	if (strstr(response_copy, "HTTP/1.1 200 OK")) {
		char* aux = strstr(response_copy, "[{\"");
		printf("%s\n", aux);
	} 	
	if (strstr(response_copy, "Expired")) {
		(*access) = 0;
		printf("%s\n", response);
	} else {
		printf("%s\n", response);
	}

	close(sockfd);
}

void get_book(char* cookie, char* token, int* access) {
	int sockfd = open_connection(IP_SERVER, PORT_SERVER, AF_INET, SOCK_STREAM, 0);
	char* api_msg = calloc(100, sizeof(char));
	WARNING(api_msg == NULL, "Nu se mai poate aloca memorie pentru api_msg get_book");
	strcpy(api_msg, "/api/v1/tema/library/books/");
	char book_number[50];
	printf("id: ");
	scanf("%s", book_number);
	strcat(api_msg, book_number);
	
	char* msg = compute_get_request(IP_SERVER, api_msg,
	                                 "application/json", cookie, token, 1);

	send_to_server(sockfd, msg);

	char* response = receive_from_server(sockfd);
	char* response_copy = calloc(strlen(response) + 1, sizeof(char));
	WARNING(response_copy == NULL, "Nu se mai poate aloca memorie pentru copia raspunsului");
	strcpy(response_copy, response);
	
	if (strstr(response_copy, "HTTP/1.1 200 OK")) {
		printf("%s\n", response); //aici ar trebui strstr doar pentru obtinerea cartii
	}
	if (strstr(response_copy, "Expired")) {
		(*access) = 0;
		printf("%s\n", response);
	} else {
		printf("%s\n", response);
	}

	close(sockfd);
}


void add_book(char* cookie, char* token, int* access) {
	JSON_Value *root_val = json_value_init_object();
	JSON_Object *root_obj = json_value_get_object(root_val);
	char* reading_line = calloc(BUFLEN, sizeof(char));
	printf("title: ");
	scanf("%s", reading_line);
	json_object_set_string(root_obj, "title", reading_line);
	printf("author: ");
	scanf("%s", reading_line);
	json_object_set_string(root_obj, "author", reading_line);
	printf("publisher: ");
	scanf("%s", reading_line);
	json_object_set_string(root_obj, "publisher", reading_line);
	printf("genre: ");
	scanf("%s", reading_line);
	json_object_set_string(root_obj, "genre", reading_line);
	printf("page_count: ");
	scanf("%s", reading_line);
	json_object_set_string(root_obj, "page_count", reading_line);

	//punem aici pentru a fi siguri ca
	//nu expira conexiunea pana terminam de completat toate detaliile cartii
	int sockfd = open_connection(IP_SERVER, PORT_SERVER, AF_INET, SOCK_STREAM, 0);
	char* message = compute_post_request(IP_SERVER, "/api/v1/tema/library/books",
		"application/json", json_serialize_to_string_pretty(root_val),
		strlen(json_serialize_to_string_pretty(root_val)), cookie, token);
	send_to_server(sockfd, message);
    char* response = receive_from_server(sockfd);
	char* response_copy = calloc(strlen(response) + 1, sizeof(char));
	WARNING(response_copy == NULL, "Nu se mai poate aloca memorie pentru copia raspunsului");
	strcpy(response_copy, response);
	if (strstr(response_copy, "Expired")) {
		(*access) = 0;
	}
	printf("%s\n", response);
	json_value_free(root_val);
	close(sockfd);
}

void delete_book(char* cookie, char* token, int* access) {
	int sockfd = open_connection(IP_SERVER, PORT_SERVER, AF_INET, SOCK_STREAM, 0);
	char* api_msg = calloc(100, sizeof(char));
	WARNING(api_msg == NULL, "Nu se mai poate aloca memorie pentru api_msg get_book");
	strcpy(api_msg, "/api/v1/tema/library/books/");
	char book_number[50];
	printf("id: ");
	scanf("%s", book_number);
	strcat(api_msg, book_number);
	
	char* msg = compute_get_request(IP_SERVER, api_msg,
	                                 "application/json", cookie, token, 0);
	send_to_server(sockfd, msg);
	char* response = receive_from_server(sockfd);
	char* response_copy = calloc(strlen(response) + 1, sizeof(char));
	
	WARNING(response_copy == NULL, "Nu se mai poate aloca memorie pentru copia raspunsului");
	strcpy(response_copy, response);
	if (strstr(response_copy, "Expired")) {
		(*access) = 0;
	}
	printf("%s\n", response);
	close(sockfd);
}


void logout(char* cookie, char* token, int* logged_in) {
	int sockfd = open_connection(IP_SERVER, PORT_SERVER, AF_INET, SOCK_STREAM, 0);

	char* msg = compute_get_request(IP_SERVER, " /api/v1/tema/auth/logout",
	                                 "application/json", cookie, NULL, 1);

	send_to_server(sockfd, msg);

	char* response = receive_from_server(sockfd);
	if (strstr(response, "HTTP/1.1 200 OK")) {
		//daca ne-am delogat trebuie sa "uitam" toate informatiile retinute pe
		//parcurs
		memset(cookie, 0, strlen(cookie));
		memset(token, 0, strlen(token));	
	}
	printf("%s\n", response);
	if (strstr(response, "HTTP/1.1 200 OK")) {
		(*logged_in) = 0;
	}

	close(sockfd);
}


char *compute_post_request(char *host, char *url, char* content_type, char* data,
                           int content_length, char* cookie, char* JWT_token) {
	char *message = calloc(BUFLEN, sizeof(char));
	WARNING(message == NULL, "Nu se mai poate aloca memorie pentru mesaj");

	char *line = calloc(LINELEN, sizeof(char));
	WARNING(message == NULL, "Nu se mai poate aloca memorie pentru linie");


	// Step 1: write the method name, URL and protocol type
	sprintf(line, "POST %s HTTP/1.1", url);
	compute_message(message, line);

	// Step 2: add the host
	memset(line, 0, LINELEN);
	sprintf(line, "Host: %s", host);
	compute_message(message, line);

	/* Step 3: add necessary headers (Content-Type and Content-Length are mandatory)
	        in order to write Content-Length you must first compute the message size
	*/
	memset(line, 0, LINELEN);
	sprintf(line, "Content-Type: %s", content_type);
	compute_message(message, line);
	memset(line, 0, LINELEN);
	sprintf(line, "Content-Length: %d", content_length);
	compute_message(message, line);

	// Step 4.1 : add cookie
	if ((JWT_token != NULL) && (cookie[0] != 0)) {
		memset(line, 0, LINELEN);
		sprintf(line, "Authorization: Bearer %s", JWT_token);
		compute_message(message, line);
	}

	// Step 4.2 : add cookie
	if ((cookie != NULL) && (cookie[0] != 0)) {
		memset(line, 0, LINELEN);
		sprintf(line, "Cookie: %s", cookie);
		compute_message(message, line);
	}
	// Step 5: add new line at end of header
	compute_message(message, "");

	// Step 6: add the actual payload data
	compute_message(message, data);
	compute_message(message, "");


	free(line);
	return message;
}

char *compute_get_request(char *host, char *url, char *query_params,
                          char *cookie, char* JWT_token, int type) {
    char *message = calloc(BUFLEN, sizeof(char));
    WARNING(message == NULL, "Nu se mai poate aloca memorie pentru mesaj");
  
    char *line = calloc(LINELEN, sizeof(char));
    WARNING(message == NULL, "Nu se mai poate aloca memorie pentru linie");

    // Step 1: write the method name, URL, request params (if any) and protocol type
    if (type) {
    	if (query_params != NULL) {
        	sprintf(line, "GET %s?%s HTTP/1.1", url, query_params);
    	} else {
        sprintf(line, "GET %s HTTP/1.1", url);
    	}
    } else {
    	//DELETE request
    	if (query_params != NULL) {
        sprintf(line, "DELETE %s?%s HTTP/1.1", url, query_params);
    	} else {
        	sprintf(line, "DELETE %s HTTP/1.1", url);
    	}
    }
    compute_message(message, line);

    // Step 2: add the host
    memset(line, 0, LINELEN);
    sprintf(line, "Host: %s", host);
    compute_message(message, line);
    
    // Step 3.1 (optional) : add token
	if ((JWT_token != NULL) && (JWT_token[0] != 0)) {
		memset(line, 0, LINELEN);
		sprintf(line, "Authorization: Bearer %s", JWT_token);
		compute_message(message, line);
	}

    // Step 3.2 (optional): add headers and/or cookies, according to the protocol format
    if ((cookie != NULL) && (cookie[0] != 0)) {
        memset(line, 0, LINELEN);
		sprintf(line, "Cookie: %s", cookie);
		compute_message(message, line);
    }
    // Step 4: add final new line
    compute_message(message, "");
    return message;
}