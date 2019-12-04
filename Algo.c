#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

typedef struct playerPerformance {
	int gameID;
	char date[12];
	int playerID;
	char playerName[50];
	char ownTeam[25];
	char opponent[25];
	char start;
	char home;
	float minutesPlayed;
	float usageRate;
	char rest[2]; //Need to be able to handle 3+ as a result.
	char position[2];
	int price;
	float dfsPoints;
	} pP;
typedef struct averageStats {
	int playerID;
	double averagePoints;
	double stdev;
	char playerName[50];
	char playerTeam[25];
	double averagePrice;
	double averageValue;
	double fiftyFiftyValue;
	double shaneStat;
} avstats;
typedef struct playerInfo {
	int playerID;
	char playerName[50];
	char ownTeam[25];
	char position[2];
} pINF;

int getNumberOfPlayerPerformancesInData();
pP* setDataArray(int pPCount);
void displayData(avstats *a);
pINF* setUniquePlayersArray (pP *data, int pPCount);
pINF* removeBadData(pINF* playerIDs);
avstats *trueNumbers(pINF* playerIDArray, pP* data, int pPCount);
pINF *setPlayerInfo(pINF* playerIDArray, pP*data, int pPCount);
avstats *sortByAvePoints(avstats* a);


int main() {

int pPCount = getNumberOfPlayerPerformancesInData();
pP *data = setDataArray(pPCount);
pINF* playerIDs = setUniquePlayersArray(data, pPCount);
pINF* ActualPlayerArray = removeBadData(playerIDs);
avstats* idAveStDev = trueNumbers(playerIDs, data, pPCount);
idAveStDev = sortByAvePoints(idAveStDev);
displayData(idAveStDev);
return 0;
}

int getNumberOfPlayerPerformancesInData() {
	int playerPerformanceCount = 0;
	FILE *dta = fopen("nbaDFSdata.csv", "r");
	char newLineDetector = getc(dta);
	while (newLineDetector != EOF) {
		if (newLineDetector == '\n') {
		playerPerformanceCount++;
		}
		newLineDetector = getc(dta);
	}
	return(playerPerformanceCount);
}
pP* setDataArray(int pPCount) { //Reads the data from the file and inputs it into a large array.
	int i;
	pP* data;
	data = (pP*) malloc(pPCount *sizeof(pP));
	FILE *file = fopen("nbaDFSdata.csv", "r");
	
	for (i=0; i<pPCount; i++) {
		fscanf(file, "%d%*c", &data[i].gameID);
		fscanf(file, "%[^\n,]%*c", data[i].date);
		fscanf(file, "%d%*c", &data[i].playerID);
		fscanf(file, "%[^\n,]%*c", data[i].playerName);
		fscanf(file, "%[^\n,]%*c", data[i].ownTeam);
		fscanf(file, "%[^\n,]%*c", data[i].opponent);
		fscanf(file, "%c%*c", &data[i].start);
		fscanf(file, "%c%*c", &data[i].home);
		fscanf(file, "%f%*c", &data[i].minutesPlayed);
		fscanf(file, "%f%*c", &data[i].usageRate);
		fscanf(file, "%[^\n,]%*c", data[i].rest);
		fscanf(file, "%[^\n,]%*c", data[i].position);
		fscanf(file, "%d%*c", &data[i].price);
		fscanf(file, "%f%*c", &data[i].dfsPoints);

	}
	
	return(data);
}
void displayData(avstats *a) {
	int i;
	printf(" Name\t\t\t\t Team\t\t  Average DFS Points\t Standard Deviation\t   Average Price       Average Value	   FiftyfiftyValue	Shane Stat\n");
	for (i=0;i<175;i++) {
		printf("-");
	}
	printf("\n");
	for (i=0; i<420;i++) {
		//printf("%s, ", a[i].playerPosition);
		//printf("%d\t ", a[i].playerID);
		printf("%-30s ", a[i].playerName);
		printf("%-18s\t ", a[i].playerTeam);
		
		printf("%5.2f\t", a[i].averagePoints);
		printf("%20.2f\t ", a[i].stdev);
		printf("%20.2f\t ", a[i].averagePrice);
		printf("%15.3f\t", a[i].averageValue);
		printf("%15.3f\t", a[i].fiftyFiftyValue);
		printf("%15.3f\n", a[i].shaneStat);
	} 
}
pINF* setUniquePlayersArray (pP *data, int pPCount) {
	int i, j, k, uniquePlayers, checker, m, n;
	checker = 0;
	uniquePlayers = 0;
	pINF* playerIDs = (pINF*) malloc(1000 * sizeof(pINF));
	
	if (data[0].playerID) { //If there is a player in the data set the first component of the playerIDs array to be that because it will be unique.
		playerIDs[0].playerID = data[0].playerID;
		strcpy(playerIDs[0].playerName, data[0].playerName);
		strcpy(playerIDs[0].ownTeam, data[0].ownTeam);
		strcpy(playerIDs[0].position, data[0].position);
		
		uniquePlayers++;
	}
	
	for (i=1;i<pPCount;i++) {
		for (j=0;j<uniquePlayers;j++) {
			if (data[i].playerID == playerIDs[j].playerID) { //checks if the current player has already been accounted for
				checker++;
			}
		}
			if (checker == 0) { //if there was no account of this player id in the playerIDs array
				playerIDs[uniquePlayers].playerID = data[i].playerID;
				strcpy(playerIDs[uniquePlayers].playerName,data[i].playerName);
				strcpy(playerIDs[uniquePlayers].ownTeam, data[i].ownTeam);
				strcpy(playerIDs[uniquePlayers].position, data[i].position);
				uniquePlayers++;
			}
			checker=0;
	}
	
	
	return (playerIDs);
}
pINF* removeBadData(pINF* playerIDs) { //Gets rid of the bad data from the previous function. A filter. Some of the playerIDs were single digits which were unreadable data.
	int i = 0;
	int uniquePlayers = 0;
	pINF* realUniquePlayers = (pINF*) malloc(1000 *sizeof(pINF)); //Not dynamically allocated but make it later.
	
	while (playerIDs[i].playerID) {
		if (playerIDs[i].playerID>2000) {
			realUniquePlayers[uniquePlayers].playerID = playerIDs[i].playerID;
			uniquePlayers++;
			i++;
		}
		else i++;
	}

	return (realUniquePlayers);
}
avstats *trueNumbers(pINF* playerIDArray, pP* data, int pPCount) {
	int i=0;
	int j,k,l,m;
	double totpoints = 0;
	int gamesPlayedCount = 0;
	double average, dev, totdev, standardDeviation, totprice, avprice, avValue,fiftyVal;
	avstats* stats = (avstats*) malloc(1000000 * sizeof(avstats*));
	totdev = 0;
	totprice = 0;
	
	while (playerIDArray[i].playerID) {
		for (j=0;j<pPCount;j++) {
			if (playerIDArray[i].playerID == data[j].playerID) {
				totpoints = totpoints + data[j].dfsPoints;
				gamesPlayedCount++;
				totprice = totprice + data[j].price;
			}
		}
		average = totpoints/gamesPlayedCount; //Finds Average DFS points, DFS price.
		for (l=0;l<gamesPlayedCount;l++) {
			dev = data[j].dfsPoints - average;
			totdev = totdev + dev;
			
		}
		standardDeviation = sqrt(fabs(totdev)/gamesPlayedCount);
		avprice = totprice/gamesPlayedCount;
		avValue = average*1000/avprice;
		fiftyVal = average/standardDeviation;
		
		
		for (m=0;m<pPCount;m++) { //Matched playerID with player name and places the data into the array for display
			if (playerIDArray[i].playerID == data[m].playerID) {
				strcpy(stats[i].playerName, data[m].playerName);
				strcpy(stats[i].playerTeam, data[m].ownTeam);
			}
		}
			//Placing data into display array	
		stats[i].playerID = playerIDArray[i].playerID;
		stats[i].averagePoints = average;
		stats[i].stdev = standardDeviation;
		stats[i].averagePrice = avprice;
		stats[i].averageValue = avValue;
		stats[i].fiftyFiftyValue = fiftyVal;
		stats[i].shaneStat = fiftyVal * avValue;
		
		//reset values
		i++;
		totpoints = 0;
		totdev = 0;
		totprice = 0;
		gamesPlayedCount = 0;
	}
	return(stats);
}
avstats *sortByAvePoints(avstats* a) { 
	int i,j;
	avstats swap;	

	for (i=0;i<420;i++) {
		for (j=i+1; j<420;j++) {
			if (a[i].shaneStat<a[j].shaneStat) {
				swap = a[i];
				a[i]= a[j];
				a[j]=swap;
			}
		}
	}
	
	return (a);
}