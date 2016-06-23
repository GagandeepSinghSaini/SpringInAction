package many_to_many_bi;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
@Entity
@Table(name="Books_bi")
public class Books {

	@Id
	@GeneratedValue
	private long bookId;
	@Column(name="bookName")
	private String bookName;
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="AuthorBook", joinColumns=@JoinColumn(name="bookId"), inverseJoinColumns=@JoinColumn(name="auhorId") )
	private Set<Author> author;
	public long getBookId() {
		return bookId;
	}
	public void setBookId(long bookId) {
		this.bookId = bookId;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public Set<Author> getAuthor() {
		return author;
	}
	public void setAuthor(Set<Author> author) {
		this.author = author;
	}
	
}
