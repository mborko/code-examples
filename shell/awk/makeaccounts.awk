BEGIN{
  record=0
    counter=0
    identifier="Gruppe "
}
{
  while (getline < groupFile)
  {
    if ($0 == "$identifier$groupNr")
    {
      record=1
#continue
    }

#echo $0

    if (record == 1)
    {
      if ($0 | egrep $identifier >>/dev/null)
      {
	break
      }

      if ($0 == "")
      { 
	continue
      }

      data=groupNr groupFile studentFile password

	data=data cat studentFile | awk -F '{if ($1==groupNr){ print " "$5; exit 0 }}'`

	student[counter]=data
	counter=counter+1
    }
  }


  passwd=cat $password | if ($1=='$1'){ print $2; exit 0 }


  j=0


    while (j le counter)
    {
      print student[j] | print "INSERT INTO users(group_id,username,password,email,lastname,firstname,kennzahl,email) VALUES('groupNr','\''nwsgroupNr'\'',SHA1('\'passwd\''),'\''studentFile'\'','\''password'\'','groupNr','\''"$5"'\'')"

	j=j+1
    }

