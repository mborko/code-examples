#include <stdarg.h>
#include <stdio.h>

void my_error(FILE *out, const char *fmt, ...)
{
    va_list ap;
    va_start(ap, fmt);
    vfprintf(out, fmt, ap);
    va_end(ap);
}

int main (int argc, char** argv)
{
	my_error(stdout, "das ist ein test\n1.Zahl: %d, 2.Zahl: %d, 3.Zahl: %d\n", 5, 6, 7);
	return 0;
}

