package com.polymorphous.util.mapGen;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.polymorphous.ai.util.PathFinder;
import com.polymorphous.main.Game;

public class GenMapTest {

	@Test
	public void seedTest() {
		// Test to ensure that seeds generate identical maps
		int mapSize = 200;
		GenMap map1 = new GenMap(mapSize, mapSize, 99999l);
		GenMap map2 = new GenMap(mapSize, mapSize, 99999l);
		
		Node[][] map1Nodes = map1.getNodes();
		Node[][] map2Nodes = map2.getNodes();
		
		for(int y = 0; y < mapSize; y++) {
			for(int x = 0; x < mapSize; x++) {
				assertSame(map1Nodes[x][y].getTileType(), map2Nodes[x][y].getTileType());
			}
		}
	}
	
	@Test
	public void loadTest() {
		// Test to ensure that saving a map and loading it generates an identical map to the original
		int mapSize = 200; 
		GenMap map = new GenMap(mapSize,mapSize);
		Node[][] map1Nodes = map.getNodes();
		// Save the map to png
		SaveMap.writeMap(map.getWidth(), map.getHeight(), map.getNodes(), "TestMap.png");
		// Load the same map from png
		GenMap map2 = LoadMap.readImage("TestMap.png").get();
		Node[][] map2Nodes = map2.getNodes();
				
		for(int y = 0; y < mapSize; y++) {
			for(int x = 0; x < mapSize; x++) {
				assertSame(map1Nodes[x][y].getTileType(), map2Nodes[x][y].getTileType());
			}
		}
	}
	
	@Test
	public void traverseTest() {
		// Ensures that all spawn points are traversable from all other spawn points on the map 
		int testNum = 3;
		int mapSize = 20;
		
		for(int i = 0; i < testNum; i++) {
			
			boolean traversable = false;
			
			GenMap map = null;
			
			while(!traversable){
				traversable = !traversable;
				map = new GenMap(mapSize, mapSize);
	        
	        	ArrayList<Node> spawns = map.getSpawns();
	        	for (int k = 0; k < spawns.size() - 1; k++) {
	            	if (PathFinder.pathFind(spawns.get(k), spawns.get(k + 1), map).size() <= 1) {
	                	traversable = false;
	                	break;
	            	}
	        	}
			}
			// O(n^4) complexity! 
			for(int y1 = 0; y1 < mapSize; y1++) {
				for(int x1 = 0; x1 < mapSize; x1++) {
					for(int y2 = 0; y2 < mapSize; y2++) {
						for(int x2 = 0; x2 < mapSize; x2++) {
							if(y1 != y2 && x1 != x2 
									&& (map.getItem(x1, y1).getTileType() != MapCode.WALL && map.getItem(x2, y2).getTileType() != MapCode.WALL)) {
								assertNotEquals(PathFinder.pathFind(map.getItem(x1, y1), map.getItem(x2, y2), map).size(),0);
							}
						}
					}
				}
			}
			
		}
		
	}

}
