/**
 * This file is part of MobilIT.
 *
 * MobilIT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MobilIT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MobilIT. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @See https://github.com/sim51/mobilIT
 */
package fr.mobilit.neo4j.server.pojo;

/**
 * Class that reprensent a point of interest.
 * 
 * @author bsimard
 * 
 */
public class POI {

    private String   id;
    private String   name;
    private GeoPoint geoPoint;
    private String   geocode;

    /**
     * Constructor.
     * 
     * @param id
     * @param name
     * @param osm_node_id
     * @param longitude
     * @param latitude
     * @param geocode
     */
    public POI(String id, String name, Double longitude, Double latitude, String geocode) {
        super();
        this.id = id;
        this.name = name;
        this.geoPoint = new GeoPoint(longitude, latitude);
        this.geocode = geocode;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the geocode
     */
    public String getGeocode() {
        return geocode;
    }

    /**
     * @param geocode the geocode to set
     */
    public void setGeocode(String geocode) {
        this.geocode = geocode;
    }

    /**
     * @return the geoPoint
     */
    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    /**
     * @param geoPoint the geoPoint to set
     */
    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

}
