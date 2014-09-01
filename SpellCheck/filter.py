import sys

all_words = open('all-words.txt', 'r')
for line in all_words:
	if line[0].isupper():
		sys.stdout.write('')
	else:
		sys.stdout.write(line)
