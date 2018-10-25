# include <stdio.h>
# include <time.h>

int sudoku [9] [9];

int setzeFelder [9]={0,0,0,3,3,3,6,6,6};
int i,j,k,o,zahl,r,t,wert,x,dL;
int darfichquer () {
    for (o=0;o<9;o++) {
      if(sudoku[i][o]==zahl||sudoku[i][o]==-zahl) {
        return 0;
      }
    }
  return 1;
}
  int darfichsenkrecht () {
    for (k=0;k<9;k++) {
      if(sudoku[k][j]==zahl||sudoku[k][j]==-zahl) {
        return 0;
      }
    }
   return 1;
  }
  int darfichkasten (int y, int x) {
    x=setzeFelder[x];
    y=setzeFelder[y];

    if(
    sudoku[y][x]==zahl     || sudoku[y][x]==-zahl         ||
    sudoku[y][x+1]==zahl   ||   sudoku[y][x+1]==-zahl     ||
    sudoku[y][x+2]==zahl   ||   sudoku[y][x+2]==-zahl     || 
    sudoku[y+1][x]==zahl   ||   sudoku[y+1][x]==-zahl     ||
    sudoku[y+1][x+1]==zahl ||     sudoku[y+1][x+1]==-zahl ||
    sudoku[y+1][x+2]==zahl ||     sudoku[y+1][x+2]==-zahl ||
    sudoku[y+2][x]==zahl   ||   sudoku[y+2][x]==-zahl     ||
    sudoku[y+2][x+1]==zahl ||     sudoku[y+2][x+1]==-zahl ||
    sudoku[y+2][x+2]==zahl || sudoku[y+2][x+2]==-zahl
    ) {
    return 0;
    }
    return 1;
  }
int loese (int y,int x,int dasEine,int rueckwaerts)  {
         dL++;
         wert=sudoku[y][x];

         if(wert<0&&rueckwaerts==0)
           return 1;
         if(wert<0&&rueckwaerts==1)
           return 0;

         if(wert>=0) {
           if(dasEine==1)
             zahl=wert+1;
           else
             zahl=1;

          while(zahl<=9) {
            if((darfichquer()==1&&darfichsenkrecht()==1&&darfichkasten(y,x)==1)) {
              sudoku[y][x]=zahl;
              return 1;
            }else{
              zahl++;
            }
          }
          if(sudoku[y][x]==wert) { 
           return 0;
          }
        }
        return 1;
}

void druckeSudoku () {
     int waag,senk;
              for(senk=0;senk<9;senk++) {
                for(waag=0;waag<9;waag++) {
                  if(sudoku[senk][waag]>0)
                    printf("|%d",sudoku[senk][waag]);
                  else
                    printf("|%d",sudoku[senk][waag]*-1);
                }
                printf("|\n");
              }
              printf("\n");
}

int main (int argc,char ** argv) {
  printf("Der Sudoku-Hacker\n");
  
  int A,B,input;  
  for (A=0;A<9;A++) {
    for(B=0;B<9;B++) {
      printf("Geben Sie bitte die Reihe %d Spalte %d Zahl an",A+1,B+1);
      scanf("%d",&input);
      if(input!=0)
        input=0-input;
        sudoku[A][B]=input;
    }
  }
  printf("\n");
 
     for (i=0;i<9;i++) {//j=x,i=y
       for (j=0;j<9;j++) {
           x=loese(i,j,0,0);
         while(x==0){
           if(sudoku[i][j]>0)
             sudoku[i][j]=0;
           if(j==0) {
               i--;
               j=8;
               x=loese(i,j,1,1);
           }else{
             j--;
             x=loese(i,j,1,1);
           }
         }
       }
     }
     druckeSudoku();
     printf("Sudoku in %d Durchläufen geknackt\n",dL);
 return 0;
}

