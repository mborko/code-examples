DEPS = $(wildcard *.h)
OBJ = $(patsubst *.c, *.o, $(wildcard *.c))

%.o : %.c $(DEPS)
	gcc -c -o $@ $< -I.

compile: $(OBJ)
	gcc -o sudoku $^ -I.

run:
	./sudoku

clean:
	@rm *.o sudoku

