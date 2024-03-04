package kjy.myshop.domain;

public class ProductVO {

   private int    pnum;       // 제품번호
   private String    pname;      // 제품명
   private int     fk_cnum;    // 카테고리코드(Foreign Key)의 시퀀스번호 참조
   private String  pimage;    // 제품이미지   이미지파일명
   private int    pqty;       // 제품 재고량
   private int    price;      // 제품 가격
   private String    pcontent;   // 제품설명 
   private String    pdetail;    // 제품구성                                        
   
   
   private CategoryVO categvo; // 카테고리VO 
   
   
   
   
   public ProductVO() { }
   
   public ProductVO(int pnum, String pname, int fk_cnum, String pcompany, 
                  String pimage, 
                  int pqty, int price, 
                  String pcontent, String pdetail) {
   
      this.pnum = pnum;
      this.pname = pname;
      this.fk_cnum = fk_cnum;
      this.pimage = pimage;
      this.pqty = pqty;
      this.price = price;
      this.pcontent = pcontent;
      this.pdetail = pdetail;
      
   }

   
   public int getPnum() {
      return pnum;
   }

   public void setPnum(int pnum) {
      this.pnum = pnum;
   }

   public String getPname() {
      return pname;
   }

   public void setPname(String pname) {
      this.pname = pname;
   }

   public int getFk_cnum() {
      return fk_cnum;
   }

   public void setFk_cnum(int fk_cnum) {
      this.fk_cnum = fk_cnum;
   }


   public String getPimage() {
      return pimage;
   }

   public void setPimage(String pimage) {
      this.pimage = pimage;
   }

   
   public int getPqty() {
      return pqty;
   }

   public void setPqty(int pqty) {
      this.pqty = pqty;
   }

   public int getPrice() {
      return price;
   }

   public void setPrice(int price) {
      this.price = price;
   }


   public String getPcontent() {
      return pcontent;
   }

   public void setPcontent(String pcontent) {
      this.pcontent = pcontent;
   }


   public CategoryVO getCategvo() {
      return categvo;
   }

   public void setCategvo(CategoryVO categvo) {
      this.categvo = categvo;
   }

   public String getPdetail() {
      return pdetail;
   }

   public void setPdetail(String pdetail) {
      this.pdetail = pdetail;
   }


   
}