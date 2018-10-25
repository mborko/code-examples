#include <soci/soci.h>
#include <soci/postgresql/soci-postgresql.h>
#include <iostream>
#include <istream>
#include <ostream>
#include <string>
#include <exception>

using namespace soci;
using namespace std;

bool get_name(string &name)
{
	cout << "Enter name: ";
	return cin >> name;
}

int main()
{
	try
	{
		session sql(postgresql, "hostaddr=192.168.188.129 dbname=schokofabrik user=schokouser password=schokoUser");

		int count;
		sql << "select count(*) from auftrag", into(count);

		cout << "We have " << count << " entries in \"auftrag\".\n";

		string name;
		while (get_name(name))
		{
			string address;
			indicator ind;
			sql << "select adresse from kunde where firmenname = :name",
			    into(address, ind), use(name);

			if (ind == i_ok)
			{
				cout << "The Address is " << address << '\n';
			}
			else
			{
				cout << "There is no adresse for " << name << '\n';
			}
		}
	}
	catch (exception const &e)
	{
		cerr << "Error: " << e.what() << '\n';
	}
}
