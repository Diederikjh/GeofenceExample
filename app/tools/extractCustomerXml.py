#!/usr/bin/python

import argparse
import sqlite3

def printXMLCustomerData(dbFilePath):

	with open('out.xml', 'w') as f:
		f.write("<customers>\n")

		conn = sqlite3.connect(dbFilePath);
		cursor = conn.cursor()
		for row in cursor.execute(''' SELECT customer.id, customer.name, location.latitude, location.longitude, location.accuracy FROM customer JOIN 
			location on customer.location_id = location.id '''):
			f.write(" <customer id='%s' name='%s' latitude='%s' longitude='%s' accuracy='%s' />\n" % row)

		conn.close()
		f.write("</customers>\n")

parser = argparse.ArgumentParser("Converts Honeybee customer data to XML as undeerstood by Geofence example app")
parser.add_argument("filepath", help="Path to SQLite databse to be extracted")
args = parser.parse_args()
printXMLCustomerData(args.filepath)



