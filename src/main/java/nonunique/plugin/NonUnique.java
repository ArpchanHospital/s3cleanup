package nonunique.plugin;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "cleanup")
public class NonUnique extends AbstractMojo
{

    @Parameter( property = "cleanup.bucketName", required = true)
    private String bucketName;

    @Parameter( property = "cleanup.artifactPath", required = true)
    private String artifactPath;

    public void execute()
        throws MojoExecutionException
    {
        final AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        ObjectListing objects = s3Client.listObjects(bucketName, artifactPath);
        for (S3ObjectSummary objectSummary : objects.getObjectSummaries())
        {
            System.out.println("deleting key:"+objectSummary.getKey());
            s3Client.deleteObject(bucketName, objectSummary.getKey());
        }
        System.out.println("Cleaned up all files from:"+bucketName+artifactPath);
    }
}
