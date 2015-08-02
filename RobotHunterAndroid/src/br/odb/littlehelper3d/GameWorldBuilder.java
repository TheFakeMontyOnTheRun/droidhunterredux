package br.odb.littlehelper3d;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import br.odb.libstrip.MeshFactory;
import br.odb.utils.Color;
import br.odb.utils.FileServerDelegate;
import br.odb.utils.Utils;

public class GameWorldBuilder {

	GameWorld getFor(String levelName, FileServerDelegate fsd,
			MeshFactory meshFactory) {


		String line;
		line = "";

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fsd.openAsset(levelName)));
			line = br.readLine();

			while (line != null) {
				parseLine(line, fsd, meshFactory);
				line = br.readLine();
			}

			fsd.log("GameWorldBuilder", "loaded " + sectorList.size()
					+ " sector(s) with no issues to report");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		GameSector[] sector = sectorList.toArray( new GameSector[ 1 ] );
		
		GameWorld toReturn = new GameWorld( sector );
		
		return toReturn;
	}

	GameSector sector;

	private void parseLine(String line, FileServerDelegate server,
			MeshFactory factory) {

		String[] tokens = Utils.tokenize(line, " ");
		float vals[] = new float[7];

		switch (line.charAt(0)) {
		case 'm':

			vals[0] = Float.parseFloat(tokens[1]);
			vals[1] = Float.parseFloat(tokens[2]);
			vals[2] = Float.parseFloat(tokens[3]);
			vals[3] = Float.parseFloat(tokens[4]);
			vals[4] = Float.parseFloat(tokens[5]);
			vals[5] = Float.parseFloat(tokens[6]);

			sector = makeSector(vals[0], vals[0] + vals[1], vals[2], vals[2]
					+ vals[3], vals[4], vals[4] + vals[5], factory);
			sector.setIsMaster(line.charAt(0) == 'm');
			sector.setId(sectorList.size());

			sectorList.add(sector);

			break;

		case 'c':

			sector.setColor(Color.getColorFromHTMLColor(tokens[2]),
					Integer.parseInt(tokens[1]));
			break;
		case 'p':
		case 'l':
			sector.setLinks(Integer.parseInt(tokens[1]),
					Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]),
					Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]),
					Integer.parseInt(tokens[6]));

			break;
		case 'n': {

			int n = Integer.parseInt(tokens[1]);

			if (n > 0) {

			}
		}
			break;

		case 'd':

			// makeDoorAt(sector, Integer.parseInt(tokens[1]),
			// Integer.parseInt(tokens[2]), null);
			break;
		case 't':

			// if (sector.isMaster()) {
			// applyDecalTo(server, sector, Integer.parseInt(tokens[1]),
			// tokens[2]);
			// }
			break;
		case 'i':
			sector.setExtraInformation(line.substring(2));
			break;
		case 'r':
			sector.name = line.substring(2);
			break;
		}
	}

	private ArrayList<GameSector> sectorList;

	public int getTotalSectors() {
		return sectorList.size();
	}

	public GameWorldBuilder() {
		sectorList = new ArrayList<GameSector>();
	}

	public void applyDecalTo(FileServerDelegate server, GameSector sector,
			int face, String decalName) {

		sector.setDecalAt(face, decalName);
	}

	public GameSector makeSector(float x0, float x1, float y0, float y1,
			float z0, float z1, MeshFactory factory) {
		return new GameSector(x0, x1, y0, y1, z0, z1);
	}

	public boolean contains(GameSector sector) {
		return this.sectorList.contains(sector);
	}
}
