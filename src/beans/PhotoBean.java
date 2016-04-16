package beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

import dao.PhotoDAO;
import dto.Photo;

public class PhotoBean {
	private UploadedFile file;
	private Photo photo = new Photo();

	public UploadedFile getFile() {
		System.out.println("getFile");
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
		System.out.println("setFile");
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public String getPictureName() {
		String name = "no-image.png";
		if (file != null) {
			name = file.getFileName();
		}
		return name;
	}

	public List<Photo> getPhotos() {
		return PhotoDAO.allPhotos();
	}
	
	public void upload() {
		System.out.println("upload1");
		if (file != null) {
			System.out.println("upload succes");
			File tmp = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("images") + "\\"
					+ file.getFileName());
			try {
				InputStream is = file.getInputstream();
				OutputStream os = new FileOutputStream(tmp);

				byte[] buff = new byte[1024];
				int length;

				while ((length = is.read(buff)) > 0) {
					os.write(buff, 0, length);
				}

				is.close();
				os.close();

				//TODO
				photo.setAuthor("vaske");
				photo.setPath(file.getFileName());

				if (PhotoDAO.insert(photo)) {
					FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
					FacesContext.getCurrentInstance().addMessage(null, message);
					FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("slika");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("upload error");
			FacesMessage message = new FacesMessage("File isn't uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		File f = new File("asdsa.txt");
		System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRealPath("images"));

	}
}
