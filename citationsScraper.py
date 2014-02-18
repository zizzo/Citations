import urllib
import re

CAT_NAME = "politics"
OLD_CIT_FILE = "/home/luigi/Dropbox/fraluigab/programming-luigi/citations/citCategory.txt"
NEW_CIT_FILE = "/home/luigi/Dropbox/fraluigab/programming-luigi/citations/newCitCategory.txt"
LINK_TO_SCRAPE = "http://www.brainyquote.com/quotes/topics/topic_politics9.html"
 
'''
Put a file with the citations of the category you wish to enrich. 
Respecting the established format the method scrapes a website (brainyquotes) and creates a new file extended with the new citations
'''
def scrape():
    
    oldQuotes = []
    
    f = open(OLD_CIT_FILE, "r")
    content = f.read()
    f.close()
    
    #Load the content of each quote in an array
    regex = re.compile("<string.*?>\"(.*?)\"</string>")
    quotes = regex.findall(content)
    for quote in quotes:
        oldQuotes.append(quote.split(" - "))

    #Load the whole old quotes in an array
    regex = re.compile("<string.*?>\".*?\"</string>")
    oldQuotesStrings = regex.findall(content)
    
    
    #Scrape the webpage to get the new quotes
    opener = urllib.request.build_opener()
    opener.addheaders = [('User-agent', 'Mozilla/5.0')]
    urllib.request.install_opener(opener)
    content = str(urllib.request.urlopen(LINK_TO_SCRAPE).read())
    
    regex = re.compile("<div class=\"boxyPaddingBig\">.*?</div>")
    quotes = regex.findall(content)
    
    #Prepare the regex to be used to extract each new quote from the HTML and the name of the authors
    regexQuote = re.compile("<a.*?>(.*?)</a>")
    regexAuthors = re.compile(CAT_NAME + "\w+\d+")
    for quote in quotes:
        #Get a list with all the authors
        authors = regexAuthors.findall(",".join(oldQuotesStrings))
        #Get the new quote and check if it can be added
        quoAut = regexQuote.findall(quote)
        newQuote = [quoAut[0].replace("\\", ""), quoAut[1].replace("\\", "")]
        
        if newQuote[0] not in oldQuotes and len(newQuote[0]) + len(newQuote[1]) < 140:
            #Avoid the last char of a quote being a . (it's ugly)
            if newQuote[0][-1] == ".":
                newQuote[0] = newQuote[0][:-1]
            #Create variables for the author name in the string identifier and in the sign    
            authNameArray = newQuote[1].split(" ")
            authNameInSign = ""
            #Build the sign --> I Have Many Names -- I. H. M. Names
            for name in authNameArray[:-1]:
                authNameInSign = authNameInSign + name[0] + ". "
            authNameInSign = authNameInSign + authNameArray[-1]
            #Build the formatted string using as identifier (categoryname)(LastOfManyNames)(ProgressiveNumberOfTheSameAuthor)
            #Please remember to remove the N at the of the string which is put to help you going through the file and checking only the new entries 
            newAuthorName = newQuote[1].split(" ")[-1]
            regexAuthor = re.compile("(" + newAuthorName + ")" + "(\d+)")
            newAuthorQuotes = regexAuthor.findall(','.join(authors))
            if not newAuthorQuotes:
                newAuthorName = newAuthorName + "1"
            else:
                number = newAuthorQuotes[-1][1]
                number = int(number) + 1
                newAuthorName = newAuthorName + str(number)
            #Save the formatted string
            newString = "<string name=\"" + CAT_NAME + newAuthorName + "\">\"" + newQuote[0] + " - "+ authNameInSign +"\"</string>"
            oldQuotesStrings.append(newString)
            
    #Sort and write output to a new file
    newQuotesString = sorted(oldQuotesStrings)
    f = open(NEW_CIT_FILE, "w")
    for string in newQuotesString:
        f.write(string + "\n")
    f.close()
    
    
    
    
if __name__ == "__main__":
    scrape()