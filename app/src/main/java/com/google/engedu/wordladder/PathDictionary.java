/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.wordladder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;

public class PathDictionary {
    private static final int MAX_WORD_LENGTH = 4;
    private static final int MAX_SEARCH_DEPTH = 5;
    private static HashSet<String> words = new HashSet<>();
    private static HashMap<String, ArrayList<String>> wordGraph = new HashMap<>();

    private boolean isNeighbourString(String a, String b) {
        if ((a == null) || (b == null) || (a.length() == 0) || (b.length() == 0) || (a.length() != b.length()) || (a.compareTo(b) == 0)) {
            return false;
        }

        boolean retval = false;
        int diffCount = 0;
        for (int idx = 0; idx < a.length(); ++idx) {
            if (a.charAt(idx) != b.charAt(idx)) {
                if (++diffCount > 1) {
                    break;
                }
            }
        }

        if (diffCount == 1) {
            retval = true;
        }
        return retval;
    }

    public PathDictionary(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        Log.d("Word ladder", "Loading dict");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        Log.d("Word ladder", "Loading dict");
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() > MAX_WORD_LENGTH) {
                continue;
            }
            words.add(word);
            wordGraph.put(word, new ArrayList<String>());
        }
        Iterator<String> iter = words.iterator();
        while (iter.hasNext()) {
            String vertexString = iter.next();
            Iterator<String> iter2 = words.iterator();
            while (iter2.hasNext()) {
                String neighbourString = iter2.next();
                if (isNeighbourString(vertexString, neighbourString)) {
                    wordGraph.get(vertexString).add(neighbourString);
                }
            }
        }
    }

    public boolean isWord(String word) {
        return words.contains(word.toLowerCase());
    }

    private ArrayList<String> neighbours(String word) {
        ArrayList<String> nList = wordGraph.get(word);
        if (nList == null) {
            nList = new ArrayList<String>();
        }
        return nList;
    }

    public String[] findPath(String start, String end) {
        ArrayList<String> path = new ArrayList<String>();
        ArrayDeque<ArrayList<String>> pathsExplored = new ArrayDeque<>();

        path.add(start);
        pathsExplored.add(path);
        while (! pathsExplored.isEmpty()) {
            ArrayList<String> currPath = pathsExplored.removeFirst();
            String currPathLogStr = new String();
            for (int idx = 0; idx < currPath.size(); ++idx) {
                currPathLogStr += currPath.get(idx) + " ";
            }
            Log.d("Word ladder", "currPath " + currPathLogStr);
            ArrayList<String> neighbourStrings = neighbours(currPath.get(currPath.size() - 1));
            for (int idx = 0; idx < neighbourStrings.size(); ++idx) {
                String neighbour = neighbourStrings.get(idx);
                if (neighbour.compareTo(end) == 0) {
                    Log.d("Word ladder", "Found complete path");
                    currPath.add(end);
                    String[] retArray = new String[currPath.size()];
                    retArray = currPath.toArray(retArray);
                    return retArray;
                }
            }

            if (currPath.size() >= MAX_SEARCH_DEPTH) {
                continue;
            }

            for (int idx = 0; idx < neighbourStrings.size(); ++idx) {
                String neighbour = neighbourStrings.get(idx);
                ArrayList<String> newPath = null;
                if (! currPath.contains(neighbour)) {
                    newPath = new ArrayList<String>(currPath);
                    newPath.add(neighbour);
                    pathsExplored.add(newPath);
                }
            }
        }
        return null;
    }
}
