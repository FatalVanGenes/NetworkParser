from random import randint
from random import randrange

networkCount = 1
subnetMasterCount = 10
subnetCount = 0
linkCount = 0
indentLevel = 0
indents = [
'',
'\t',
'\t\t',
'\t\t\t',
'\t\t\t\t',
'\t\t\t\t\t',
'\t\t\t\t\t\t',
'\t\t\t\t\t\t\t',
'\t\t\t\t\t\t\t\t',
'\t\t\t\t\t\t\t\t\t',
'\t\t\t\t\t\t\t\t\t\t',
'\t\t\t\t\t\t\t\t\t\t\t',
'\t\t\t\t\t\t\t\t\t\t\t\t',
'\t\t\t\t\t\t\t\t\t\t\t\t\t',
'\t\t\t\t\t\t\t\t\t\t\t\t\t\t',
'\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t',
]

nodeIdx = 1000
# profilePointCount = 1300
profilePointCount = 20

def printNetwork():
	global subnetCount, subnetMasterCount
	id = 910
	print('<?xml version="1.0" encoding="UTF-8"?>')
	print('<network name="' + 'my_network' + '" id="' + str(id) + '">')
	printProfileData()
	while subnetCount < subnetMasterCount:
		printSubnets()
	printLinks()
	printAttrs('network')
	print('</network>')
	return

attrValues = [
'else',
'this is quite a long value oh yes it is',
'1',
'here is another rather long value but who am I to judge',
'linxM34'
]

def printProfileData():
	global indents, indentLevel, profilePointCount

	prefix='profile'
	indentLevel += 1
	for idx in range(0, profilePointCount):
		print(indents[indentLevel] + '<profile name="' + prefix + 'something_' + str(idx) + '" value="' + attrValues[randint(0,3)]+ '">')
		indentLevel += 1
		for x2 in range(0, 24):
			for x3 in range(0, 60):
				print(indents[indentLevel] + '<point x="' + str(x2 * 60 + x3) + '" y="' + str(randrange(100000, 88888888)) + '"/>')
		indentLevel -= 1
		print(indents[indentLevel] + '</profile>')
	indentLevel -= 1
	return
		

def printAttrs(prefix):
	global indents, indentLevel
	indentLevel += 1
	attrCount = randint(35,65)
	for idx in range(1, attrCount):
		print(indents[indentLevel] + '<attr name="' + prefix + 'something_' + str(idx) + '" value="' + attrValues[randint(0,3)]+ '"/>')
	indentLevel -= 1
	return

def printLinks():
	global indents, indentLevel, subnetCount

	linkCount = 2 * subnetCount
	indentLevel += 1
	for idx in range(1, linkCount):
		print(indents[indentLevel] + '<link name="' + 'link' + '_' + str(idx) + '" id="' + str(idx) + '">')
		printAttrs('link')
		print(indents[indentLevel] + '</link>')
	indentLevel -= 1
	return

def scaleUp():
	tmp = randint(0,100)
	if tmp <= 50:
		return 15
	if tmp <= 60:
		return 35
	if tmp <= 70:
		return 55
	if tmp <= 80:
		return 75
	if tmp < 90:
		return 95
	return 110

def printNodes():
	global nodeIdx, indents, indentLevel
	indentLevel += 1
	nodeCount = scaleUp()
	for idx in range(1, nodeCount):
		print(indents[indentLevel] + '<node name="' + 'node' + '_' + str(nodeIdx) + '" id="' + str(nodeIdx) + '">')
		nodeIdx += 1
		printAttrs('node')
		print(indents[indentLevel] + '</node>')
	indentLevel -= 1
	return

def printSubnets():
	global subnetCount
	global indents, indentLevel

	indentLevel += 1
	for subnetIdx in range(1, randint(5,9)):
		subnetCount += 1
		print(indents[indentLevel] + '<subnet name="' + 'subnet' + '_' + str(subnetCount) + '" id="' + str(subnetCount) + '">')
		printNodes()
		printAttrs('subnet')
		if subnetIdx == 4:
			if subnetCount < subnetMasterCount:
				printSubnets()
		print(indents[indentLevel] + '</subnet>')
	indentLevel -= 1
	return

printNetwork()

