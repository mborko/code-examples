#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>
#include <math.h>

int main() {
    char num[100];
    int decimal = 0, i, j, base, len;
    
    printf("Enter a number: ");
    if (fgets(num, sizeof(num), stdin) == NULL) {
        printf("Error reading input.\n");
        return 1;
    }
    
    len = strlen(num);
    
    // Remove newline character if present
    if (num[len-1] == '\n') {
        num[len-1] = '\0';
        len--;
    }
    
    // Determine the base of the input number
    if (len > 0 && num[0] == '0') {
        if (len > 1 && (num[1] == 'b' || num[1] == 'B')) {
            base = 2;
            j = 2;
        } else if (len > 1 && (num[1] == 'o' || num[1] == 'O')) {
            base = 8;
            j = 2;
        } else if (len > 1 && (num[1] == 'x' || num[1] == 'X')) {
            base = 16;
            j = 2;
        } else {
            printf("Invalid input.\n");
            return 1;
        }
    } else {
        base = 10;
        j = 0;
    }
    
    // Convert the input number to decimal
    for (i = len-1; i >= j; i--) {
        if (isdigit(num[i])) {
            decimal += (num[i] - '0') * pow(base, len-i-1);
        } else if (isxdigit(num[i])) {
            decimal += (toupper(num[i]) - 55) * pow(base, len-i-1);
        } else {
            printf("Invalid input.\n");
            return 1;
        }
    }
    
    // Output the results
    printf("Decimal: %d\n", decimal);
    printf("Binary: %ld\n", strtol(num, NULL, base == 2 ? 2 : 10));
    printf("Octal: %lo\n", strtol(num, NULL, base == 8 ? 8 : 10));
    printf("Hexadecimal: %lX\n", strtol(num, NULL, base == 16 ? 16 : 10));
    
    return 0;
}

