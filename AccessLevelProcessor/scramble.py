import csv
import string
import random

"""
Scrambles existing enrollment data (original not provided) to make
protected student info (name and ID) anonymous. Used only to make
the larger csv processing project public. Not necessary to run this process
for the rest of the script to work.

Input file "enrollment.csv" expected to have the following fields:
Last Name, First Name, ID, Course Subject,
Course Number, Course Section, Course Title
"""
# read "enrollment.csv", output "scrambled_info.csv"
def write_new_enrollment_file():
    with open('enrollment.csv') as f:
        contents = csv.DictReader(f)
        new_names = []
        for row in contents:
            new_first = scramble(row['First Name'])
            new_last = scramble(row['Last Name'])
            new_id = scramble(row['ID'])
            line = {'Last Name':new_last, 'First Name':new_first, 'ID':new_id,
                    'Course Subject':row['Course Subject'],
                    'Course Number':row['Course Number'],
                    'Course Section':row['Course Section'],
                    'Course Title':row['Course Title']}
            new_names.append(line)

        scramble_output_name = 'output/scrambled_info.csv'
        header = ['Last Name', 'First Name', 'ID', 'Course Subject',
                  'Course Number', 'Course Section', 'Course Title']

        with open(scramble_output_name, 'w', newline='') as f:
            fieldnames = header
            writer = csv.DictWriter(f, fieldnames = fieldnames)

            writer.writeheader()
            for i in new_names:
                writer.writerow(i)

# scrambles letters and numbers in a string
def scramble(field):
    zero_to_nine = "1234567890"
    new_field = ""
    for char in field:
        if char.isalpha():
            new_field += random.choice(string.ascii_letters)
        if char.isdigit():
            new_field += random.choice(zero_to_nine)
    return new_field

# main call
write_new_enrollment_file()
