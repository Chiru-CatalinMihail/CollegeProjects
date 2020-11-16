#include "client.h"

int main() {
    int logged_in = 0, acces = 0;
    char* input = calloc(MAXINPUT , sizeof(char));
    DIE(input == NULL, "Nu se mai poate aloca memorie pentru input");
    char* cookie = calloc(BUFLEN, sizeof(char));
    DIE(cookie == NULL, "Nu se mai poate aloca memorie pentru cookie");
    char* JWT_token = calloc(BUFLEN, sizeof(char));
    DIE(JWT_token == NULL, "Nu se mai poate aloca memorie pentru token");

    while (1) {
        memset(input, 0, sizeof(input)*sizeof(char));
        scanf("%s", input);

        /* exit este prima comanda deoarece la indeplinirea ei,
        executia clientului se termina */
        if (!strcmp(input, "exit")) {
            free(input);
            printf("Comanda de exit a fost efectuata cu succes :D\n");
            break;
        } else if (!strcmp(input, "register")) {
            reg();
            continue;
        } else if (!strcmp(input, "login")) {
            if (logged_in) {
                printf("Esti deja conectat la un cont! Nu te poti reconecta");
                printf("cata vreme esti on! :/\n");
            } else {
                login(cookie, &logged_in);
            }
            continue;

        } else if (!strcmp(input, "enter_library")) {
            if (logged_in) {
            	enter_library(cookie, JWT_token, &acces);
            } else {
            	printf("Trebuie sa fii conectat la server pentru a accesa");
            	printf(" biblioteca :(\n");
            }
            continue;

        } else if (!strcmp(input, "get_book")) {
            if (logged_in && acces) {
            	get_book(cookie, JWT_token, &acces);
            } else {
            	printf("Nu ai dat login sau nu mai ai acces la biblioteca\n");
            	printf("Pentru autentificare, foloseste comanda \"login\" \n");
            	printf("Iar pentru un nou token JWT \"enter_library\" \n");
            }
            continue;

        } else if (!strcmp(input, "get_books")) {
            if (logged_in && acces) {
            	get_books(cookie, JWT_token, &acces);
            } else {
            	printf("Nu ai dat login sau nu mai ai acces la biblioteca\n");
            	printf("Pentru autentificare, foloseste comanda \"login\" \n");
            	printf("Iar pentru un nou token JWT \"enter_library\" \n");
            }
            continue;

        } else if (!strcmp(input, "add_book")) {
            if (logged_in && acces) {
            	add_book(cookie, JWT_token, &acces);
            } else {
            	printf("Nu ai dat login sau nu mai ai acces la biblioteca\n");
            	printf("Pentru autentificare, foloseste comanda \"login\" \n");
            	printf("Iar pentru un nou token JWT \"enter_library\" \n");
            }
            continue;

        } else if (!strcmp(input, "delete_book")) {
            if (logged_in && acces) {
            	delete_book(cookie, JWT_token, &acces);
            } else {
            	printf("Nu ai dat login sau nu mai ai acces la biblioteca\n");
            	printf("Pentru autentificare, foloseste comanda \"login\" \n");
            	printf("Iar pentru un nou token JWT \"enter_library\" \n");
            }
            continue;

        } else if (!strcmp(input, "logout")) {
            if (logged_in) {
                printf("%s %s\n", JWT_token, cookie);
                logout(cookie, JWT_token, &logged_in);
            } else {
                printf("Nu poti da logout daca nu te-ai logat inca! :/\n");
            }
            continue;

        } else {
            printf("Nu exista comanda data sau a fost scrisa avand typos :/\n");
        }

    }

    free(cookie);
    free(JWT_token);
    return 0;
}