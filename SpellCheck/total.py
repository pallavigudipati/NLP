words = open('cleaned_counts.txt','r')

total = 0
for line in words:
	word_count = line.split(',')
	count = int(word_count[1])
	total += count
