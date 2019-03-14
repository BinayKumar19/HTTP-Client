# HTTP-Client
Computer Networks and Protocols Assignment (Fall-2017)

A simple HTTP client application is developed and tested with a real HTTP Servers (web servers). Basic functionalities of cURL command line are implemented in this application.
cURL is an open source command line tool and library for transferring data with URL syntax, supporting DICT, FILE, FTP, FTPS, Gopher, HTTP, HTTPS, IMAP, IMAPS, LDAP, LDAPS, POP3, POP3S, RTMP, RTSP, SCP, SFTP, SMB, SMTP, SMTPS, Telnet and TFTP. curl supports SSL certificates, HTTP POST, HTTP PUT, FTP uploading, HTTP form based upload, proxies, HTTP/2, cookies, user+password authentication (Basic, Plain, Digest, CRAM-MD5, NTLM, Negotiate and Kerberos), file transfer resume, proxy tunneling and more

Similarly to the cURL project, the requested implementation in this project have a network protocol software library and the command line application. Therefore, the main tasks of this project is to Implemente an HTTP client library using TCP Socket directly. Only a subset of HTTP protocol features are requested i.e. HTTP GET and HTTP POST requests.

Get Usage: 
  httpc help get 
  usage: httpc get [-v] [-h key:value] URL 
Get executes a HTTP GET request for a given URL. 
 
   -v             Prints the detail of the response such as protocol, status, and headers.    
   -h key:value   Associates headers to HTTP Request with the format 'key:value'. 
   
Post Usage: 
  httpc help post    
usage: httpc post [-v] [-h key:value] [-d inline-data] [-f file] URL 
 
Post executes a HTTP POST request for a given URL with inline data or from file. 
 
   -v             Prints the detail of the response such as protocol, status, and headers.    
   -h key:value   Associates headers to HTTP Request with the format 'key:value'.    
   -d string      Associates an inline data to the body HTTP POST request.    
   -f file        Associates the content of a file to the body HTTP POST request. 
    Either [-d] or [-f] can be used but not both. 
 
Bonus work:
  –o filename, which allow the HTTP client to write the body of the response to the specified file instead of the console.
   redirection option  
  
Testing Commands
1) java -jar httpc.jar help
2) java -jar httpc.jar help get
3) java -jar httpc.jar help post

4) java -jar httpc.jar get "http://httpbin.org/get?course=networking&assignment=1"
5) java -jar httpc.jar get -v "http://httpbin.org/get?course=networking&assignment=1"
6) java -jar httpc.jar get -h Content-Type:application/html "http://httpbin.org/get?course=networking&assignment=1"
7) java -jar httpc.jar get -v -h Content-Type:application/html "http://httpbin.org/get?course=networking&assignment=1"

8) java -jar httpc.jar post "http://httpbin.org/post"
9) java -jar httpc.jar post -v "http://httpbin.org/post"
10) java -jar httpc.jar post -h Content-Type:application/html "http://httpbin.org/post"
11) java -jar httpc.jar post -v -h Content-Type:application/html "http://httpbin.org/post"
12) java -jar httpc.jar post -f "E:\MS\Fall 2017\Computer networking\Lab\http_demo_file.txt" "http://httpbin.org/post"
13) java -jar httpc.jar post -v -f "E:\MS\Fall 2017\Computer networking\Lab\http_demo_file.txt" "http://httpbin.org/post"
14) java -jar httpc.jar post -h Content-Type:application/html -f "E:\MS\Fall 2017\Computer networking\Lab\http_demo_file.txt" "http://httpbin.org/post"
15) java -jar httpc.jar post -v -h Content-Type:application/html -f "E:\MS\Fall 2017\Computer networking\Lab\http_demo_file.txt" "http://httpbin.org/post"
16) java -jar httpc.jar post -d "{\"Assignment\": 1}" "http://httpbin.org/post"
17) java -jar httpc.jar post -v -d "{\"Assignment\": 1}" "http://httpbin.org/post"
18) java -jar httpc.jar post -h Content-Type:application/html -d "{\"Assignment\": 1}" "http://httpbin.org/post"
19) java -jar httpc.jar post -v -h Content-Type:application/html -d "{\"Assignment\": 1}" "http://httpbin.org/post"

Bonus Marks test cases
17) java -jar httpc.jar post -v "http://httpbin.org/post" -o output.txt
18) java -jar httpc.jar get -L "http://httpbin.org/redirect/6"
for status code 302
