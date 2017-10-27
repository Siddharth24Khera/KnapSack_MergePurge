import os
import matplotlib.pyplot as plt
import shutil

output_directory_path = "./output"
if os.path.exists(output_directory_path):
	shutil.rmtree(output_directory_path)

os.mkdir(output_directory_path)

print("\nCompiling files\n")
os.system("javac KnapSack.java")

print("Running 0/1 KnapSack\n")
os.system("java KnapSack > output/result.txt")

point_list_string = ''
point_list_array = []

with open("output/result.txt","r") as outFile:
	for line in outFile:
		if line[0] =='(' and line[1] == '0':
			point_list_string = line
			break
		print(line,end='')

point_list_string = point_list_string[:len(point_list_string)-2]

point_pairs = point_list_string.split('&')

for pair in point_pairs:
	pair = pair.lstrip('(').rstrip(')').split(",")
	pair = [int(pair[1]),int(pair[0])]
	point_list_array.append(pair)

# print(point_list_array)
x_Array= [i[0] for i in point_list_array]
y_Array= [i[1] for i in point_list_array]

# plt.plot(x_Array, y_Array, 'ro')
# plt.scatter(x_Array,y_Array,s = 10)
plt.step(x_Array,y_Array)
plt.ylabel('Optimal Profit')
plt.xlabel('Weight')

print("\nGenerating grapha\n")

print("Execution Complete. Graph displayed")

plt.show()
