
Task description: create an application capable to read words in given files and break them into 4 categories by first letter. Categorized words and total count of each one shall be saved to separate files. Upon user request application must be able to provide statistict of how many times each word has been matched. Application must be multithreaded.



Implementation:

Application is using Spring framework. Once started it launches a RESTful webservice and a frontend powered by a combination of AngularJS and Bootstrap. WS maps a few addresses: 

* / (POST) - new files are uploaded here. Once file is uploaded it is passed on to an individual instance of file parser: it breaks the file to separate words and stores them in a Map<String, Long> as keys. Each following word occurence increments the 'Long' value in the Map. That way all the words are counted. Once file is parsed the Map is passed on to a singleton of ResultMapDispatcherWorker thread. This guy fires up 4 workers of its own (minions if you will) each being responsible for separate category of words (nice way to avoid critical section). Dispatcher iterates through list of words in the given map and tries to guess which category it can be dropped into. Once category is determined Dispatched passes that word (and count of occurrences) to a minion responsible for that particular category. The minion, being a nice guy he is, stores those values in its own Map. In case minion already has that word it increments the occurence value by whatever Long value was given to it by Dispatcher along with the word. 

* /save (POST) - this request will force all minions to dump their Map<String, Long> buffers to files in the filesystem.

* /results (GET) - this is the right door to knock on if you need to see what words have already been parsed and how many of each of them was there.

* /results/word/{word} (GET) - this is where you query for a particular word (!CASE SENSITIVE!) to see how many times it has been uploaded.

* /results/category/{cat} (GET) - similar to above except here you must provide a category of words to query for. Result is an array of JSONs.

* /categories (GET) - returns an array of JSONs containing all available categories along with their regex patterns.




Do you see that gradle.build file in the project directory? Yes, that's right! I'm naughty - I do gradle. In case you are interested to see how the app works - pull it to your workstation, change cwd to the project dir (where that gradle.build lies in) and fire 'gradle jettyRun' command (or 'gradlew jettyRun' if you do Windows). It will hive you a hint for what to enter in your web browser (please tell me it's not IE). Go there and give the app a spin.

Oh, by the way... If you do indeed do windows a few code modifications will be required before starting the project up. Open up src/main/java/com/homework/objects/Constants.java and change "workdir" value to whatever suits your filesystem. This is where matched files will be stored at. If you don't do that you will get a few nasty exceptions by the time you try to start minions. MACs, Linux and UNIX platforms should be good to go with defaults though. Yes, I could set this tunable to use user homedir from JVM params. But I didn't. Apparently System.getProperty('user.home') was just too much to ask for... :) (just kidding)


And a few words about the UI. Try not to look directly at it or else your eyes might start on fire (don't tell me I didn't warn you!). Yes, it's that ugly. However it does the job. It is running on AngularJS framework and popping nice buttons and lists according to Bootstrap's taste. I could have made a nice JSP page instead of old-fasioned HTML+JS one but I haven't. And I have a good reason for it. In fact a few:

a) saving server-side processing power. Imagine a world where everyone wanted to know what words their files contain and how many of them are there. And just like you open up cnn, 15min or whatever (news portal you are using) first thing in the morning (and a few more times while you're at work... come on, we all know you do that!), in that hipothetical world upload their random files to this WS right after morning shower (and at work when boss is not looking). That's one crazy world, ain't it? That means this little soft and fuzzy application would have to generate individual webpage for each request! With all the stylings and stuff.. Is that Lean? I surely don't think so. Lean tells us to give the customer exactly what he wants and try to waste as little resources as possible. So lets do that! If the customes asks for statistics - give him statistics! And a javascript which he already has loaded (by the time he opened the webpage) will do the rest. Distributed workload is the way to go.

b) saving bandwitch. Since you already are in the world a).... Networks layer is a bottleneck. A huge one actually! If you want more processing power you can get a few more servers and load-balance them. But you need more bandwitch a new gigabit (or infiniband) network card might not be enough if we are talking about thousands of request. Implementing a REST WS means that you will not have to re-send the customer all the styles, javascripts, htmls and stuff every time he wants something from you. He already has all that. All he actually needs now is the data. So give him the data! JSON and array signatures are extremely lightweight so REST WS is the way to reduce your network bills n times!

c) automation, scalability and improvements. Everything is going IT these days. Almost everyone knows how to do programming or scripting and it's a good thing. IoT is based on those little scripts. Anyone knowing how to use cUrl can develop his own little home-application allowing him to get his work done faster and in a way he really wants to. As a matter of fact multiple services working as rest APIs for different applications can be combined together and make up a brand new product! It is a perfect modular structure. 


So here it is. This application has been made in 4 days (considering I spend 12 hours at work each night and sometimes even sleep or eat). It's far not perfect, but it works. It has it all: java frameworks (Spring), RESTful webservice fundamentals, multithreading, data analysis, file uploads, UI based on two more frameworks: AngularJS and Bootstrap and a few coffee spills next to my keyboard arround 4 a.m.

Now when do we meet ?



