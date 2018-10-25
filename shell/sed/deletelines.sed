# From UNIX and Shell Programming, Behrouz A. Forouzan and Richard F. Gilberg
# Deletes text block from BEGIN to END

/BEGIN/,/END/{
	# Put BEGIN line in hold space
	/BEGIN/{
		h
		d
	}
	# lines between BEGIN & END in hold space
	/END/!{
		H
		d
	}
	# Exchang hold space and pattern space.
	/END/{
		x
		G
	}
	# pattern space contains all lines, 
        # substitute normally
	s/BEGIN.*END//
}
