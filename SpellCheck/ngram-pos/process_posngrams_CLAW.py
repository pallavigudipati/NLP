import sys
def load_mappings(fname):
	fp=open(fname)
	CLAWS_TO_POS={}
	for line in fp:
		words=line.split()
		CLAWS_TO_POS[words[1].lower()]=words[0].lower()
	return CLAWS_TO_POS
def strip_trailing_integers(s):
	leading_string=""
	lastplusone=len(s)
	for i in range(len(s)):
		if(s[i:i+1].isdigit()):
			lastplusone=i
			break
	return s[:lastplusone]
#print strip_trailing_integers("krall123");
CLAWS_TO_POS=load_mappings("claws_pos_mappings.txt")
input_file=open(sys.argv[1])
output_file=open(sys.argv[2],"w")
frequency_counts={}
for line in input_file:
	words=line.split()
	posngramcount=int(words[0])
	ngram_length=(len(words)-1)/2
	ngram=""
	#print line
	#print words
	for i in range(1,ngram_length+1):
		current_word=words[i]
		current_pos_tag=words[i+ngram_length]
		#print current_pos_tag
		transformed_pos_tag=CLAWS_TO_POS[strip_trailing_integers(current_pos_tag.lower())].upper()
		transformed_word=current_word.lower()
		ngram+=transformed_word+"_"+transformed_pos_tag+"\t"
	if ngram in frequency_counts:
		frequency_counts[ngram]+=posngramcount
	else:
		frequency_counts[ngram]=posngramcount

for key in frequency_counts:
	output_file.write(str(frequency_counts[key])+"\t"+str(key)+"\n")
