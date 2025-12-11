import java.util.*;

public class Risk_Map {
    private int numPlayers;
    private int numCountries;
    private TreeSet<Country> countries;
    private Map<String, Country> countryByName;

    public Risk_Map() {
        this.numPlayers = 2;
        this.numCountries = 0;
        this.countries = new TreeSet<Country>();
        this.countryByName = new HashMap<>();

        // create countries from risk board game
        Country Alaska = new Country("Alaska");
        numCountries++;
        Country NorthwestTerritory = new Country("Northwest Territory");
        numCountries++;
        Country Greenland = new Country("Greenland");
        numCountries++;
        Country Alberta = new Country("Alberta");
        numCountries++;
        Country Ontario = new Country("Ontario");
        numCountries++;
        Country Quebec = new Country("Quebec");
        numCountries++;
        Country WesternUnitedStates = new Country("Western United States");
        numCountries++;
        Country EasternUnitedStates = new Country("Eastern United States");
        numCountries++;
        Country CentralAmerica = new Country("Central America");
        numCountries++;

        Country Venezuela = new Country("Venezuela");
        numCountries++;
        Country Peru = new Country("Peru");
        numCountries++;
        Country Brazil = new Country("Brazil");
        numCountries++;
        Country Argentina = new Country("Argentina");
        numCountries++;

        Country Iceland = new Country("Iceland");
        numCountries++;
        Country Scandinavia = new Country("Scandinavia");
        numCountries++;
        Country GreatBritain = new Country("Great Britain");
        numCountries++;
        Country NorthernEurope = new Country("Northern Europe");
        numCountries++;
        Country WesternEurope = new Country("Western Europe");
        numCountries++;
        Country SouthernEurope = new Country("Southern Europe");
        numCountries++;
        Country Ukraine = new Country("Ukraine");
        numCountries++;

        Country NorthAfrica = new Country("North Africa");
        numCountries++;
        Country Egypt = new Country("Egypt");
        numCountries++;
        Country EastAfrica = new Country("East Africa");
        numCountries++;
        Country Congo = new Country("Congo");
        numCountries++;
        Country SouthAfrica = new Country("South Africa");
        numCountries++;
        Country Madagascar = new Country("Madagascar");
        numCountries++;

        Country Ural = new Country("Ural");
        numCountries++;
        Country Siberia = new Country("Siberia");
        numCountries++;
        Country Yakutsk = new Country("Yakutsk");
        numCountries++;
        Country Kamchatka = new Country("Kamchatka");
        numCountries++;
        Country Japan = new Country("Japan");
        numCountries++;
        Country Mongolia = new Country("Mongolia");
        numCountries++;
        Country Irkutsk = new Country("Irkutsk");
        numCountries++;
        Country China = new Country("China");
        numCountries++;
        Country India = new Country("India");
        numCountries++;
        Country Afghanistan = new Country("Afghanistan");
        numCountries++;
        Country MiddleEast = new Country("Middle East");
        numCountries++;
        Country SoutheastAsia = new Country("Southeast Asia");
        numCountries++;

        Country Indonesia = new Country("Indonesia");
        numCountries++;
        Country NewGuinea = new Country("New Guinea");
        numCountries++;
        Country WesternAustralia = new Country("Western Australia");
        numCountries++;
        Country EasternAustralia = new Country("Eastern Australia");
        numCountries++;

        // populate name map
        countryByName.put(Alaska.getName(), Alaska);
        countryByName.put(NorthwestTerritory.getName(), NorthwestTerritory);
        countryByName.put(Greenland.getName(), Greenland);
        countryByName.put(Alberta.getName(), Alberta);
        countryByName.put(Ontario.getName(), Ontario);
        countryByName.put(Quebec.getName(), Quebec);
        countryByName.put(WesternUnitedStates.getName(), WesternUnitedStates);
        countryByName.put(EasternUnitedStates.getName(), EasternUnitedStates);
        countryByName.put(CentralAmerica.getName(), CentralAmerica);

        countryByName.put(Venezuela.getName(), Venezuela);
        countryByName.put(Peru.getName(), Peru);
        countryByName.put(Brazil.getName(), Brazil);
        countryByName.put(Argentina.getName(), Argentina);

        countryByName.put(Iceland.getName(), Iceland);
        countryByName.put(Scandinavia.getName(), Scandinavia);
        countryByName.put(GreatBritain.getName(), GreatBritain);
        countryByName.put(NorthernEurope.getName(), NorthernEurope);
        countryByName.put(WesternEurope.getName(), WesternEurope);
        countryByName.put(SouthernEurope.getName(), SouthernEurope);
        countryByName.put(Ukraine.getName(), Ukraine);

        countryByName.put(NorthAfrica.getName(), NorthAfrica);
        countryByName.put(Egypt.getName(), Egypt);
        countryByName.put(EastAfrica.getName(), EastAfrica);
        countryByName.put(Congo.getName(), Congo);
        countryByName.put(SouthAfrica.getName(), SouthAfrica);
        countryByName.put(Madagascar.getName(), Madagascar);

        countryByName.put(Ural.getName(), Ural);
        countryByName.put(Siberia.getName(), Siberia);
        countryByName.put(Yakutsk.getName(), Yakutsk);
        countryByName.put(Kamchatka.getName(), Kamchatka);
        countryByName.put(Japan.getName(), Japan);
        countryByName.put(Mongolia.getName(), Mongolia);
        countryByName.put(Irkutsk.getName(), Irkutsk);
        countryByName.put(China.getName(), China);
        countryByName.put(India.getName(), India);
        countryByName.put(Afghanistan.getName(), Afghanistan);
        countryByName.put(MiddleEast.getName(), MiddleEast);
        countryByName.put(SoutheastAsia.getName(), SoutheastAsia);

        countryByName.put(Indonesia.getName(), Indonesia);
        countryByName.put(NewGuinea.getName(), NewGuinea);
        countryByName.put(WesternAustralia.getName(), WesternAustralia);
        countryByName.put(EasternAustralia.getName(), EasternAustralia);

        // set neighbors

        // North America
        Alaska.addNeighbor(NorthwestTerritory);
        Alaska.addNeighbor(Alberta);
        Alaska.addNeighbor(Kamchatka);

        NorthwestTerritory.addNeighbor(Alaska);
        NorthwestTerritory.addNeighbor(Alberta);
        NorthwestTerritory.addNeighbor(Ontario);
        NorthwestTerritory.addNeighbor(Greenland);

        Greenland.addNeighbor(NorthwestTerritory);
        Greenland.addNeighbor(Ontario);
        Greenland.addNeighbor(Quebec);
        Greenland.addNeighbor(Iceland);

        Alberta.addNeighbor(Alaska);
        Alberta.addNeighbor(NorthwestTerritory);
        Alberta.addNeighbor(Ontario);
        Alberta.addNeighbor(WesternUnitedStates);

        Ontario.addNeighbor(NorthwestTerritory);
        Ontario.addNeighbor(Alberta);
        Ontario.addNeighbor(WesternUnitedStates);
        Ontario.addNeighbor(EasternUnitedStates);
        Ontario.addNeighbor(Quebec);
        Ontario.addNeighbor(Greenland);

        Quebec.addNeighbor(Ontario);
        Quebec.addNeighbor(EasternUnitedStates);
        Quebec.addNeighbor(Greenland);

        WesternUnitedStates.addNeighbor(Alberta);
        WesternUnitedStates.addNeighbor(Ontario);
        WesternUnitedStates.addNeighbor(EasternUnitedStates);
        WesternUnitedStates.addNeighbor(CentralAmerica);

        EasternUnitedStates.addNeighbor(WesternUnitedStates);
        EasternUnitedStates.addNeighbor(Ontario);
        EasternUnitedStates.addNeighbor(Quebec);
        EasternUnitedStates.addNeighbor(CentralAmerica);

        CentralAmerica.addNeighbor(WesternUnitedStates);
        CentralAmerica.addNeighbor(EasternUnitedStates);
        CentralAmerica.addNeighbor(Venezuela);

        // South America
        Venezuela.addNeighbor(CentralAmerica);
        Venezuela.addNeighbor(Peru);
        Venezuela.addNeighbor(Brazil);

        Peru.addNeighbor(Venezuela);
        Peru.addNeighbor(Brazil);
        Peru.addNeighbor(Argentina);

        Brazil.addNeighbor(Venezuela);
        Brazil.addNeighbor(Peru);
        Brazil.addNeighbor(Argentina);
        Brazil.addNeighbor(NorthAfrica);

        Argentina.addNeighbor(Peru);
        Argentina.addNeighbor(Brazil);

        // Europe
        Iceland.addNeighbor(Greenland);
        Iceland.addNeighbor(Scandinavia);
        Iceland.addNeighbor(GreatBritain);

        Scandinavia.addNeighbor(Iceland);
        Scandinavia.addNeighbor(GreatBritain);
        Scandinavia.addNeighbor(NorthernEurope);
        Scandinavia.addNeighbor(Ukraine);

        GreatBritain.addNeighbor(Iceland);
        GreatBritain.addNeighbor(Scandinavia);
        GreatBritain.addNeighbor(NorthernEurope);
        GreatBritain.addNeighbor(WesternEurope);

        NorthernEurope.addNeighbor(GreatBritain);
        NorthernEurope.addNeighbor(Scandinavia);
        NorthernEurope.addNeighbor(SouthernEurope);
        NorthernEurope.addNeighbor(WesternEurope);
        NorthernEurope.addNeighbor(Ukraine);

        WesternEurope.addNeighbor(GreatBritain);
        WesternEurope.addNeighbor(NorthernEurope);
        WesternEurope.addNeighbor(SouthernEurope);
        WesternEurope.addNeighbor(NorthAfrica);

        SouthernEurope.addNeighbor(WesternEurope);
        SouthernEurope.addNeighbor(NorthernEurope);
        SouthernEurope.addNeighbor(Ukraine);
        SouthernEurope.addNeighbor(MiddleEast);
        SouthernEurope.addNeighbor(Egypt);
        SouthernEurope.addNeighbor(NorthAfrica);

        Ukraine.addNeighbor(Scandinavia);
        Ukraine.addNeighbor(NorthernEurope);
        Ukraine.addNeighbor(SouthernEurope);
        Ukraine.addNeighbor(MiddleEast);
        Ukraine.addNeighbor(Afghanistan);
        Ukraine.addNeighbor(Ural);

        // Africa
        NorthAfrica.addNeighbor(Brazil);
        NorthAfrica.addNeighbor(WesternEurope);
        NorthAfrica.addNeighbor(SouthernEurope);
        NorthAfrica.addNeighbor(Egypt);
        NorthAfrica.addNeighbor(EastAfrica);
        NorthAfrica.addNeighbor(Congo);

        Egypt.addNeighbor(NorthAfrica);
        Egypt.addNeighbor(EastAfrica);
        Egypt.addNeighbor(SouthernEurope);
        Egypt.addNeighbor(MiddleEast);

        EastAfrica.addNeighbor(Egypt);
        EastAfrica.addNeighbor(NorthAfrica);
        EastAfrica.addNeighbor(Congo);
        EastAfrica.addNeighbor(SouthAfrica);
        EastAfrica.addNeighbor(Madagascar);
        EastAfrica.addNeighbor(MiddleEast);

        Congo.addNeighbor(NorthAfrica);
        Congo.addNeighbor(EastAfrica);
        Congo.addNeighbor(SouthAfrica);

        SouthAfrica.addNeighbor(Congo);
        SouthAfrica.addNeighbor(EastAfrica);
        SouthAfrica.addNeighbor(Madagascar);

        Madagascar.addNeighbor(EastAfrica);
        Madagascar.addNeighbor(SouthAfrica);

        // Asia (north/central/east)
        Ural.addNeighbor(Ukraine);
        Ural.addNeighbor(Siberia);
        Ural.addNeighbor(China);
        Ural.addNeighbor(Afghanistan);

        Siberia.addNeighbor(Ural);
        Siberia.addNeighbor(Yakutsk);
        Siberia.addNeighbor(Irkutsk);
        Siberia.addNeighbor(Mongolia);
        Siberia.addNeighbor(China);

        Yakutsk.addNeighbor(Siberia);
        Yakutsk.addNeighbor(Kamchatka);
        Yakutsk.addNeighbor(Irkutsk);

        Kamchatka.addNeighbor(Yakutsk);
        Kamchatka.addNeighbor(Irkutsk);
        Kamchatka.addNeighbor(Mongolia);
        Kamchatka.addNeighbor(Japan);
        Kamchatka.addNeighbor(Alaska);

        Japan.addNeighbor(Kamchatka);
        Japan.addNeighbor(Mongolia);

        Mongolia.addNeighbor(Irkutsk);
        Mongolia.addNeighbor(Siberia);
        Mongolia.addNeighbor(China);
        Mongolia.addNeighbor(Japan);
        Mongolia.addNeighbor(Kamchatka);

        Irkutsk.addNeighbor(Siberia);
        Irkutsk.addNeighbor(Yakutsk);
        Irkutsk.addNeighbor(Kamchatka);
        Irkutsk.addNeighbor(Mongolia);

        China.addNeighbor(Mongolia);
        China.addNeighbor(Siberia);
        China.addNeighbor(Ural);
        China.addNeighbor(Afghanistan);
        China.addNeighbor(India);
        China.addNeighbor(SoutheastAsia);
        China.addNeighbor(MiddleEast);

        India.addNeighbor(MiddleEast);
        India.addNeighbor(Afghanistan);
        India.addNeighbor(China);
        India.addNeighbor(SoutheastAsia);

        Afghanistan.addNeighbor(Ukraine);
        Afghanistan.addNeighbor(Ural);
        Afghanistan.addNeighbor(China);
        Afghanistan.addNeighbor(India);
        Afghanistan.addNeighbor(MiddleEast);

        MiddleEast.addNeighbor(SouthernEurope);
        MiddleEast.addNeighbor(Egypt);
        MiddleEast.addNeighbor(EastAfrica);
        MiddleEast.addNeighbor(Afghanistan);
        MiddleEast.addNeighbor(India);
        MiddleEast.addNeighbor(China);

        SoutheastAsia.addNeighbor(China);
        SoutheastAsia.addNeighbor(India);
        SoutheastAsia.addNeighbor(Indonesia);

        // Australia region
        Indonesia.addNeighbor(SoutheastAsia);
        Indonesia.addNeighbor(NewGuinea);
        Indonesia.addNeighbor(WesternAustralia);

        NewGuinea.addNeighbor(Indonesia);
        NewGuinea.addNeighbor(EasternAustralia);

        WesternAustralia.addNeighbor(EasternAustralia);
        WesternAustralia.addNeighbor(Indonesia);

        EasternAustralia.addNeighbor(WesternAustralia);
        EasternAustralia.addNeighbor(NewGuinea);

        // populate countries set
        countries.add(Alaska);
        countries.add(NorthwestTerritory);
        countries.add(Greenland);
        countries.add(Alberta);
        countries.add(Ontario);
        countries.add(Quebec);
        countries.add(WesternUnitedStates);
        countries.add(EasternUnitedStates);
        countries.add(CentralAmerica);

        countries.add(Venezuela);
        countries.add(Peru);
        countries.add(Brazil);
        countries.add(Argentina);

        countries.add(Iceland);
        countries.add(Scandinavia);
        countries.add(GreatBritain);
        countries.add(NorthernEurope);
        countries.add(WesternEurope);
        countries.add(SouthernEurope);
        countries.add(Ukraine);

        countries.add(NorthAfrica);
        countries.add(Egypt);
        countries.add(EastAfrica);
        countries.add(Congo);
        countries.add(SouthAfrica);
        countries.add(Madagascar);

        countries.add(Ural);
        countries.add(Siberia);
        countries.add(Yakutsk);
        countries.add(Kamchatka);
        countries.add(Japan);
        countries.add(Mongolia);
        countries.add(Irkutsk);
        countries.add(China);
        countries.add(India);
        countries.add(Afghanistan);
        countries.add(MiddleEast);
        countries.add(SoutheastAsia);

        countries.add(Indonesia);
        countries.add(NewGuinea);
        countries.add(WesternAustralia);
        countries.add(EasternAustralia);


    }

    public Country getCountryByName(String name) {
        return countryByName.get(name);
    }

    public int getNumPlayers() {
        return numPlayers;
    }
    public int getNumCountries() {
        return numCountries;
    }
    public Set<Country> getCountries() {
        return countries;
    }

    public String toString(){
        String res = "";
        for (Country country : countries) {
            res += country + "\n";
            
        }
        return res;
    }


}
