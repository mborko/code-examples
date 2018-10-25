/*
 * Based on the Idea of http://stackoverflow.com/questions/1735236/how-to-write-my-own-printf-in-c
 *
 * */

#include <string.h>
#include <stdio.h>
#include <stdarg.h>

void ownprintf(const char*, ...);
char* itoa(int val, int base);

int main (int argc, char** argv)
{
	const char * teststring = "Hallo Welt";
	char dest[sizeof(teststring)];

	memcpy(dest,teststring+6,4);
	dest[4]='\0';

	printf(dest);

	printf("Das ist ein neuer String mit zwei Zahlen: %d und %d\n",5,7);
	ownprintf("Das ist ein neuer String mit zwei Zahlen: %d und %b\n",5,7);
	ownprintf("ohne\n");

	return 0;
}

void ownprintf(const char* format,...)
{
	int i, begin;
        va_list arg; 
        va_start(arg, format);

	for(i=0, begin=0; *(format+i) != '\0'; i++) {
		if(*(format+i) == '%') {
			char substr[i-begin];
			memcpy(substr,(format+begin),i-begin);
			substr[i-begin]='\0';
			printf(substr);
			i++;
			switch(*(format+i)) {
				case 'd': printf(itoa(va_arg(arg,int),10)); break;
				case 'b': printf(itoa(va_arg(arg,int),2)); break;
			}
			begin=i+1;
		}
	}
	if(begin<i) {
			char substr[i-begin];
			memcpy(substr,(format+begin),i-begin);
			substr[i-begin]='\0';
			printf(substr);
	}

	va_end(arg);
}

char* itoa(int val, int base){

        static char buf[32] = {0};

        int i = 30;

        for(; val && i ; --i, val /= base)

                buf[i] = "0123456789abcdef"[val % base];

        return &buf[i+1];

}

