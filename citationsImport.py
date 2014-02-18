import re


CAT_NAME = "politics"
NEW_CIT_FILE = "/home/luigi/Dropbox/fraluigab/programming-luigi/citations/newCitCategory.txt"
NEW_IMPORT = "/home/luigi/Dropbox/fraluigab/programming-luigi/citations/newCitImport.txt"

'''
Read a completed file with the citations and build the array to be imported in CitationsManager.java
'''
def buildImport():
    f = open(NEW_CIT_FILE, "r")
    content = f.read()
    f.close()
    
    #Load all the authors
    regex = re.compile(CAT_NAME + "(\w+\d+)")
    authors = regex.findall(content)
    
    f = open(NEW_IMPORT, "w")
    f.write("{")
    for author in authors:
        f.write("context.getString(R.string." + CAT_NAME + author + "),")
    f.write("}")
    f.close()
    
    
if __name__ == "__main__":
    buildImport()