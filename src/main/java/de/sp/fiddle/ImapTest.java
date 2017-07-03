package de.sp.fiddle;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import java.util.Properties;

/**
 * Created by Martin on 22.06.2017.
 */
public class ImapTest {

    public static void main(String[] args) {
        String server = "imap.gmail.com";
        String username =  "asv.pabst@gmail.com";
		/*
		 * !!! NOTE: The password is displayed in the console and CAN BE READ by anyone watching. !!!
		 */
        String password = "Schrobenhausen1mp";

        try {
            listMessages(server, username, password);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }



    private static void listMessages(String url, String username, String password) throws Exception {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imaps");
        try {
            System.out.println("Connecting to IMAP server: " + url);
            store.connect(url, username, password);

            Folder root = store.getDefaultFolder();
            Folder[] folders = root.list();
            System.out.println("Select a folder");
            for (int i = 0; i < folders.length; i++) {
                System.out.println("\t" + folders[i].getName());
            }

            String folderName = "INBOX";
            IMAPFolder folder = (IMAPFolder) store.getFolder(folderName);

            long afterFolderSelectionTime = System.nanoTime();
            int totalNumberOfMessages = 0;
            try {
                if (!folder.isOpen()) {
                    folder.open(Folder.READ_ONLY);
                }

				/*
				 * Now we fetch the message from the IMAP folder in descending order.
				 *
				 * This way the new mails arrive with the first chunks and older mails
				 * afterwards.
				 */
                long largestUid = folder.getUIDNext() - 1;
                int chunkSize = 500;
                for (long offset = 0; offset < largestUid; offset += chunkSize) {
                    long start = Math.max(1, largestUid - offset - chunkSize + 1);
                    long end = Math.max(1, largestUid - offset);

					/*
					 * The next line fetches the existing messages within the
					 * given range from the server.
					 *
					 * The messages are not loaded entirely and contain hardly
					 * any information. The Message-instances are mostly empty.
					 */
                    long beforeTime = System.nanoTime();
                    Message[] messages = folder.getMessagesByUID(start, end);
                    totalNumberOfMessages += messages.length;
                    System.out.println("found " + messages.length + " messages (took " + (System.nanoTime() - beforeTime) / 1000 / 1000 + " ms)");

					/*
					 * If we would access e.g. the subject of a message right away
					 * it would be fetched from the IMAP server lazily.
					 *
					 * Fetching the subjects of all messages one by one would
					 * produce many requests to the IMAP server and take too
					 * much time.
					 *
					 * Instead with the following lines we load some information
					 * for all messages with one single request to save some
					 * time here.
					 */
                    beforeTime = System.nanoTime();
                    // this instance could be created outside the loop as well
                    FetchProfile metadataProfile = new FetchProfile();
                    // load flags, such as SEEN (read), ANSWERED, DELETED, ...
                    metadataProfile.add(FetchProfile.Item.FLAGS);
                    // also load From, To, Cc, Bcc, ReplyTo, Subject and Date
                    metadataProfile.add(FetchProfile.Item.ENVELOPE);
                    // we could as well load the entire messages (headers and body, including all "attachments")
                    // metadataProfile.add(IMAPFolder.FetchProfileItem.MESSAGE);
                    folder.fetch(messages, metadataProfile);
                    System.out.println("loaded messages (took " + (System.nanoTime() - beforeTime) / 1000 / 1000 + " ms)");

					/*
					 * Now that we have all the information we need, let's print some mails.
					 * This should be wicked fast.
					 */
                    beforeTime = System.nanoTime();
                    for (int i = messages.length - 1; i >= 0; i--) {
                        Message message = messages[i];
                        long uid = folder.getUID(message);
                        boolean isRead = message.isSet(Flags.Flag.SEEN);

                        if (!isRead) {
                            // new messages are green
                            System.out.print("\u001B[32m");
                        }
                        System.out.println("\t" + uid + "\t" + message.getSubject());
                        if (!isRead) {
                            // reset color
                            System.out.print("\u001B[0m");
                        }
                    }
                    System.out.println("Listed message (took " + (System.nanoTime() - beforeTime) / 1000 / 1000 + " ms)");
                }
            } finally {
                if (folder.isOpen()) {
                    folder.close(true);
                }
            }

            System.out.println("\nListed all " + totalNumberOfMessages + " messages (took " + (System.nanoTime() - afterFolderSelectionTime) / 1000 / 1000 + " ms)");
        } finally {
            store.close();
        }
    }


}
