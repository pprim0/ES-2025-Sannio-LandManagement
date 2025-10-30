package es;

/**
 * Representa uma propriedade rústica com atributos associados à sua geometria,
 * localização administrativa e informação do proprietário.
 */
public class Propriedade {

    private int objectId;
    private double parId;
    private String parNum;
    private double shapeLength;
    private double shapeArea;
    private String geometry;
    private String owner;
    private String freguesia;
    private String municipio;
    private String ilha;

    /**
     * Construtor da classe Propriedade.
     *
     * @param objectId     Identificador único do objeto
     * @param parId        Identificador do prédio
     * @param parNum       Número do prédio
     * @param shapeLength  Perímetro da propriedade
     * @param shapeArea    Área da propriedade
     * @param geometry     Geometria em formato WKT
     * @param owner        Nome do proprietário
     * @param freguesia    Freguesia onde se localiza a propriedade
     * @param municipio    Município da propriedade
     * @param ilha         Ilha onde está situada
     */
    public Propriedade(int objectId, double parId, String parNum, double shapeLength, double shapeArea,
                       String geometry, String owner, String freguesia, String municipio, String ilha) {
        this.objectId = objectId;
        this.parId = parId;
        this.parNum = parNum;
        this.shapeLength = shapeLength;
        this.shapeArea = shapeArea;
        this.geometry = geometry;
        this.owner = owner;
        this.freguesia = freguesia;
        this.municipio = municipio;
        this.ilha = ilha;
    }

    /** @return ID do objeto */
    public int getObjectId() {
        return objectId;
    }

    /** @return Identificador do prédio */
    public double getParId() {
        return parId;
    }

    /** @return Número do prédio */
    public String getParNum() {
        return parNum;
    }

    /** @return Perímetro da propriedade */
    public double getShapeLength() {
        return shapeLength;
    }

    /** @return Área da propriedade */
    public double getShapeArea() {
        return shapeArea;
    }

    /** @return Geometria da propriedade em formato WKT */
    public String getGeometry() {
        return geometry;
    }

    /** @return Nome do proprietário */
    public String getOwner() {
        return owner;
    }

    /** @return Freguesia da propriedade */
    public String getFreguesia() {
        return freguesia;
    }

    /** @return Município da propriedade */
    public String getMunicipio() {
        return municipio;
    }

    /** @return Ilha da propriedade */
    public String getIlha() {
        return ilha;
    }
}
