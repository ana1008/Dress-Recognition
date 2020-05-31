package com.example.downloadImages;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DownloadImagesApplicationTests {

	WebDriver driver;
	int maxDownload = 250;
    int j = 1613;

	@BeforeTest
	public void invokeBrowser() throws InterruptedException, IOException, AWTException {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().deleteAllCookies();;
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(120,TimeUnit.SECONDS);
        String url = "https://www.google.com/search?q=women+wedding+dresses&tbm=isch&ved=2ahUKEwiD35nHicXpAhWsgEsFHRMMBJQQ2-cCegQIABAA&oq=women+wedding+dresses&gs_lcp=CgNpbWcQAzICCAAyAggAMgYIABAHEB4yBggAEAcQHjIECAAQHjIECAAQHjIGCAAQBRAeMgYIABAFEB4yBggAEAUQHjIGCAAQBRAeOggIABAHEAUQHlCONViDSWDnS2gBcAB4AIABpAGIAdsJkgEDMC45mAEAoAEBqgELZ3dzLXdpei1pbWc&sclient=img&ei=0YPGXsOyKqyBrtoPk5iQoAk&bih=722&biw=1519&rlz=1C1CHBF_enIN863IN863&hl=en";
        driver.get(url);

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        for(int i=0;i<30;i++) {
            jse.executeScript("window.scrollBy(0,1000)");
            //li[@class="pagination-next"]
            /*if(i!=0 && i%5==0){
                //downloadImages();
                driver.findElement(By.xpath("//li[@class=\"pagination-next\"]/a")).click();
            }*/

          Thread.sleep(2000);
        }
	}

	@Test
	public void downloadImages() throws AWTException, InterruptedException, IOException {
        List< WebElement > listOfImages = driver.findElements(By.tagName("img"));
        System.out.println("No of images: "+listOfImages.size());
        Vector<String> allUrls = new Vector<String>();
        System.out.println("Collecting Urls...");
        for(int i=0;i<listOfImages.size() && i<maxDownload;i++){
            //System.out.println(i);
            if(!listOfImages.get(i).getAttribute("src").equals("") && !(listOfImages.get(i).getAttribute("src") == null)){
                //System.out.println(listOfImages.get(i).getAttribute("src"));
                allUrls.add(listOfImages.get(i).getAttribute("src"));
            }
        }

        for(int i=0;i<allUrls.size();i++,j++){
            String str = allUrls.elementAt(i);
            if(i%100 == 0)
                System.out.println("Downloading "+(j+1)+" out of "+listOfImages.size());
            BufferedImage saveImage;
            if (str.charAt(0) == 'd') {
                byte[] imagedata = DatatypeConverter.parseBase64Binary(str.substring(str.indexOf(",") + 1));
                saveImage = ImageIO.read(new ByteArrayInputStream(imagedata));
            }else{
                URL imageURL = new URL(str);
                saveImage = ImageIO.read(imageURL);
            }
            ImageIO.write(saveImage, "png", new File("images/"+(j+1)+".png"));
        }
	}

    @AfterTest
    public void endSession(){
        driver.quit();
    }


}
