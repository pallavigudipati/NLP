            String typedPhraseRaw=terminalInput.nextLine();
            ArrayList<Integer> typoPositions=new ArrayList<Integer>();
            String[] phraseWordArrayRaw = typedPhraseRaw.split(" "); 
            for(int i=0;i<phraseWordArrayRaw.length;i++)
            {
            	if(!bktree.wordDictionary.containsKey(phraseWordArrayRaw[i]))
            	{
            		typoPositions.add(i);
            	}
            }

