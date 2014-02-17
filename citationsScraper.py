import urllib
import re
 
 
'''
Put a file with the citations of the category you wish to enrich. 
Respecting the established format the method scrapes a website and add the new citations to the file
'''
def scrape():
    
    oldQuotes = []
    newQuotes = []
    
    f = open("/home/luigi/Dropbox/fraluigab/programming-luigi/citations/citCategory.txt", "r")
    content = f.read()
    regex = re.compile("<string.*?>\"(.*?)\"</string>")
    quotes = regex.findall(content)
    for quote in quotes:
        oldQuotes.append(quote.split(" - "))
    f.close()
    
    
    opener = urllib.request.build_opener()
    opener.addheaders = [('User-agent', 'Mozilla/5.0')]
    urllib.request.install_opener(opener)
    content = str(urllib.request.urlopen('http://www.brainyquote.com/quotes/topics/topic_inspirational.html').read())
    
    regex = re.compile("<div class=\"boxyPaddingBig\">.*?</div>")
    quotes = regex.findall(content)
    
    regex = re.compile("<a.*?>(.*?)</a>")
    for quote in quotes:
        quoAut = regex.findall(quote)
        newQuote = [quoAut[0].replace("\\", ""), quoAut[1].replace("\\", "")]
        if newQuote not in oldQuotes:
            newQuotes.append(newQuote)
    
    
    print(newQuotes)
    
    
    
if __name__ == "__main__":
    scrape()