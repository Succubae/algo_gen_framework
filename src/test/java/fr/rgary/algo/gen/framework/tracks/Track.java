package fr.rgary.algo.gen.framework.tracks;

import fr.rgary.algo.gen.framework.trigonometry.Line;
import fr.rgary.algo.gen.framework.trigonometry.Point;
import fr.rgary.algo.gen.framework.trigonometry.Zone;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Track.
 */
public class Track {
    private static final Logger LOGGER = LoggerFactory.getLogger(Track.class);
    public List<Line> borderOne;
    public List<Line> borderTwo;
    public List<Line> supplementalBorderOne;
    public List<Line> supplementalBorderTwo;
    public List<Line> fitnessZoneLines;
    public Point startPoint;
    public List<Line> borders;
    public List<Zone> zones;
    public static Track instance;

    private Track() {
    }

    public Track(List<Line> borderOne, List<Line> borderTwo, List<Line> fitnessZoneLines) {
        this.borderOne = borderOne;
        this.borderTwo = borderTwo;
        this.fitnessZoneLines = fitnessZoneLines;
    }

    public void init() {
        Track.instance = this;
//        Constant.setTRACK(this);
        this.borders = new ArrayList<>();
        this.borders.addAll(borderOne);
        this.borders.addAll(borderTwo);
        this.borders.addAll(supplementalBorderOne);
        this.borders.addAll(supplementalBorderTwo);
        this.zones = new ArrayList<>();

        //CREATE MORE ZONES FROM ADDITIONAL LINES
        for (int i = 0; i < supplementalBorderOne.size(); i++) {
            int a1x = supplementalBorderOne.get(i).S.X;
            int a1y = supplementalBorderOne.get(i).S.Y;
            int b1x = supplementalBorderOne.get(i).E.X;
            int b1y = supplementalBorderOne.get(i).E.Y;

            int a2x = supplementalBorderTwo.get(i).S.X;
            int a2y = supplementalBorderTwo.get(i).S.Y;
            int b2x = supplementalBorderTwo.get(i).E.X;
            int b2y = supplementalBorderTwo.get(i).E.Y;

            int m1x = a1x + ((b1x - a1x) / 2);
            int m1y = a1y + ((b1y - a1y) / 2);
            int m2x = a2x + ((b2x - a2x) / 2);
            int m2y = a2y + ((b2y - a2y) / 2);

            if (i == 0) {
                Line firstLine = new Line(supplementalBorderOne.get(i).S, supplementalBorderTwo.get(i).S);
                this.fitnessZoneLines.add(firstLine);
            }

            Line middleLine = new Line(new Point(m1x, m1y), new Point(m2x, m2y));
            this.fitnessZoneLines.add(middleLine );
            Line thirdLine = new Line(supplementalBorderOne.get(i).E, supplementalBorderTwo.get(i).E);
            this.fitnessZoneLines.add(thirdLine);
        }


        for (int i = 1; i < fitnessZoneLines.size(); i++) {
            this.zones.add(new Zone(i -1,
                    fitnessZoneLines.get(i - 1).S,
                    fitnessZoneLines.get(i - 1).E,
                    fitnessZoneLines.get(i).S,
                    fitnessZoneLines.get(i).E));
        }
//        int zoneCount = this.zones.size();


    }

    public int getZoneNumberPerPosition(Point position) {
        for (Zone zone : zones) {
            if (zone.polygon.contains(position.X, position.Y)) {
                return zone.zoneNumber;
            }
        }
        return -1;
//        throw new InternalError("YOU SHOULD NOT COME HERE, WERE ARE YOU ?!");
    }

    public void addToBorderOne(Line line) {
        if (this.supplementalBorderOne == null) {
            this.supplementalBorderOne = new ArrayList<>();
        }
        this.supplementalBorderOne.add(line);
        this.borderOne.add(line);
        this.borders.add(line);
    }

    public void addToBorderTwo(Line line) {
        if (this.supplementalBorderTwo == null) {
            this.supplementalBorderTwo = new ArrayList<>();
        }
        this.supplementalBorderTwo.add(line);
        this.borderTwo.add(line);
        this.borders.add(line);
    }

    public void printSupplementalLines() {
        LOGGER.info("{}", Strings.join(this.supplementalBorderOne, ','));
        LOGGER.info("{}", Strings.join(this.supplementalBorderTwo, ','));
    }


}
