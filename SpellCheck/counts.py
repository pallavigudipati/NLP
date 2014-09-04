words = open('ngrams/count_big.txt','r')

bigrams = {}
unigrams = {}

for line in words:
	word_count = line.split()
	word = word_count[0]
	count = int(word_count[1])

	for char in word:
		if unigrams.has_key(char):
			unigrams[char] = unigrams[char] + count
		else:
		 	unigrams[char] = count

	for i in range(0,len(word) - 1):
		bigram = word[i] + word[i + 1]
		if bigrams.has_key(bigram):
			bigrams[bigram] = bigrams[bigram] + count
		else:
		 	bigrams[bigram] = count


unigram_file = open('unigram_counts.txt','w')
bigram_file = open('bigram_counts.txt','w')

unigram_keys = unigrams.keys()
for key in unigram_keys:
	unigram_file.write(key + "," + str(unigrams[key]) + '\n')

bigram_keys = bigrams.keys()
for key in bigram_keys:
	bigram_file.write(key + "," + str(bigrams[key]) + '\n')
