# gnu make manual :: http://www.gnu.org/software/make/manual/make.html
# 4.4.3 The Function wildcard
PROJ := sortieren
OBJECTS := $(patsubst %.c, %.o, $(wildcard *.c))

$(OBJECTS):
	gcc -c *.c -std=c99

all: $(OBJECTS)
	gcc $(OBJECTS) -o $(PROJ)

run: all
	./$(PROJ)

clean:
	@rm *.o $(PROJ)

