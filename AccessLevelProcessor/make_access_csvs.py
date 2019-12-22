import csv

"""
Created for Python 3.x
Author: Matthew Fishel for the Stanford University Dept. of Art & Art History
2015

Creates quarterly Access Level batch processing CSV's for Dept of Art & Art
History facilities at Stanford University. Requires the following data
to operate, derived from existing available reports in PeopleSoft:

enrollment.csv
    fields: "Last Name","Firsrt Name","ID","Course Subject","Course Number",
            "Course Section", "Course Title"
rooms.csv
    fields: "Course Subject","Course Number","Course Section","Course Title",
            "Component", "Room", "Days", "Time", "Instructor"
accesslevels.csv
    fields: "room", "access1", "access2"

produces intermediary output files for validation and final ouput "upload.csv"
"""
# creates a condensed courseID string
def make_courseid(row):
    c_sub = str(row['Course Subject'])
    c_sub = c_sub.replace(' ','')
    c_num = str(row['Course Number'])
    c_num = c_num.replace(' ','')
    c_sec = str(row['Course Section'])
    c_sec = c_sec.replace(' ','')
    courseid =  c_sub + '-' + c_num + '-' + c_sec
    return courseid

# returns a list of dictionaries, each with room data for a course
def get_section_room():
    with open('rooms.csv') as f:
        contents = csv.DictReader(f)
        section_and_room = []
        for row in contents:
            courseid = make_courseid(row)
            l = {'course-id':courseid,'course-title':row['Course Title'],'room':row['Room']}
            if l not in section_and_room:
                section_and_room.append(l)
        return section_and_room

# adds room information to the intermediary "name-id-section.csv"
def add_rooms():
    allrooms = make_all_rooms()
    section_dict = make_section_dict()
    itr = csv.DictReader(open('output/name-id-section.csv'))
    d = []
    for i in itr:
        courseid = (i['course-id'])
        coursid_ = courseid + '_'
        room = section_dict.get(courseid,'no-room')
        room2 = section_dict.get(coursid_,'no-room')
        if room in allrooms:
            i['room'] = room
        else:
            i['room'] = ''
        if room2 in allrooms:
            i['room2'] = room2
        else:
            i['room2'] = ''
        d.append(i)
    return d

# makes a list of all rooms
def make_all_rooms():
    itr = csv.DictReader(open('accesslevels.csv'))
    rooms = []
    for i in itr:
        l = i['room']
        if l not in rooms:
            rooms.append(l)
    return rooms

# make a dictionary of section:room key-value pairs
def make_section_dict():
    itr = csv.DictReader(open('output/section-room.csv'))
    d = {}
    for i in itr:
        new_d = i['course-id']
        if new_d not in d:
            d[new_d] = i['room']
        else:
            a = new_d + '_'
            d[a] = i['room']
    return d
# make a dictionary of room:[access1, access2] key-value pairs
def make_access_dict():
    itr = csv.DictReader(open('accesslevels.csv'))
    d = {}
    for i in itr:
        new_d = i['room']
        d[new_d] = [i['access1'],i['access2']]
    return d

# writes the intermediary "name-id-section.csv" from base data
def write_name_id_section():
    with open('enrollment.csv') as f:
        contents = csv.DictReader(f)
        name_and_section = []
        for row in contents:
            courseid =  make_courseid(row)
            l = {'last':row['Last Name'],'first':row['First Name'],
                 'id':row['ID'],'course-id':courseid,
                 'course-title':row['Course Title']}
            name_and_section.append(l)

    enroll_out_fname = 'output/name-id-section.csv'
    header = ['last','first','id','course-id','course-title']

    with open(enroll_out_fname, 'w', newline='') as f:
        fieldnames = header
        writer = csv.DictWriter(f, fieldnames=fieldnames)

        writer.writeheader()
        for i in name_and_section:
            writer.writerow(i)

# writes intermediary "section-room.csv" from base data
def write_section_room():
    section_and_room = get_section_room()
    room2out_fname = 'output/section-room.csv'
    header = ['course-id','course-title','room']

    with open(room2out_fname, 'w', newline='') as f:
        fieldnames = header
        writer = csv.DictWriter(f, fieldnames=fieldnames)

        writer.writeheader()
        for i in section_and_room:
            writer.writerow(i)

# writes an intermediary "merged.csv" with name, id, course and access info
def write_merged():
    access_dict = make_access_dict()
    merged = add_rooms()

    for i in merged:
        access = access_dict.get(i['room'],'')
        access2 = access_dict.get(i['room2'],'')
        if access != '':
            i['access1'] = access[0]
            i['access2'] = access[1]
            n = 3
            if access2 != '':
                if access2[0] != access[0] and access2[0] != access[1]:
                    key_name = 'access' + str(n)
                    i[key_name] = access2[0]
                    n += 1
                if access2[1] != access[0] and access2[1] != access[1]:
                    key_name = 'access' + str(n)
                    i[key_name] = access2[1]
    out_file = 'output/merged.csv'
    header = ['last','first','id','course-id','course-title','room','access1','access2','room2','access3']

    with open(out_file,'w', newline='') as f:
        fieldnames = header
        writer = csv.DictWriter(f, fieldnames=fieldnames)

        writer.writeheader()
        for i in merged:
            writer.writerow(i)

# writes "upload.csv", formatted for batch upload
def write_upload():
    startdate = input('Start date (yyyy-mm-dd)?: ')
    enddate = input('End date (yyyy-mm-dd)?: ')
    with open('output/merged.csv') as f:
        merged = csv.DictReader(f)
        output = []
        for i in merged:
            if i['access1'] != '':
                row = [i['access1'],i['id'],'assign',startdate,enddate]
                if row not in output:
                    output.append(row)
    with open('output/merged.csv') as f:
        merged = csv.DictReader(f)
        for i in merged:
            if i['access2'] != '':
                row = [i['access2'],i['id'],'assign',startdate,enddate]
                if row not in output:
                    output.append(row)
    with open('output/merged.csv') as f:
        merged = csv.DictReader(f)
        for i in merged:
            if i['access3'] != '':
                row = [i['access3'],i['id'],'assign',startdate,enddate]
                if row not in output:
                    output.append(row)
    with open('output/upload.csv','w', newline='') as f:
        fieldnames= ['access','id','assign','start','end']
        writer = csv.writer(f)
        for i in output:
            writer.writerow(i)

# method calls
write_name_id_section()
write_section_room()
write_merged()
write_upload()
