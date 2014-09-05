fp=open("count_1edit.txt")
outfile=open("cleaned_count_1edit.txt","w")
for line in fp:
	words=line.split()
	if len(words)>2:
		continue
	else:
		if len(words[0])==1:
			continue
		elif words[0][-1]=="|":
			outfile.write(words[0]+"~"+","+words[1]+"\n")
		elif words[0][0]=="|":
			outfile.write("~"+words[0]+","+words[1]+"\n")
		else:
			outfile.write(words[0]+","+words[1]+"\n")
		
