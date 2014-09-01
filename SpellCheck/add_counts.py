all_words = open('all-words-cleaned.txt', 'r')
words = {}
for line in all_words:
	line = line.strip('\n')
	words[line] = '0'

counts = open('ngrams/count_1w.txt', 'r')
for line in counts:
	parts = line.split('\t')
	if (len(parts) < 2):
		parts = parts[0].split(' ')
#print parts[0]

	if words.has_key(parts[0]):
		words[parts[0]] = parts[len(parts) - 1].strip('\n')

zero_count = 0
keys = words.keys()

for key in keys:
	print key + ',' + words[key]	
#	if words[key] == '@':
#		zero_count = zero_count + 1

#print words
