BEGIN { print "Gesamtsumme"
	total = 0
}
	{ total += $1 }
END { print "--------"
	print "Total Sales", total }
	
