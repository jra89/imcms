DelegatingByTypeDocumentService
===============================

In this article:
    - `Introduction`_
    - `Use API`_
    - `Description parameters`_
    - `Description about Document`_



Introduction
------------
Imcms works smartly. Now we don't have to worry and create unnecessary pieces of code for editing documents, because the system took care of us
We can easy to make manipulation any type documents with using this API.
Imcms provides list API which wrote below.

Use API
-------

For what in order to get instance ``DelegatingByTypeDocumentService`` need to use Imcms.getServices().getDocumentService();

.. code-block:: jsp

     Imcms.getServices().getDocumentService().countDocuments(); //get counts documents from db;

     Imcms.getServices().getDocumentService().get(int docId);

     Imcms.getServices().getDocumentService().createNewDocument(DocumentType type, Integer parentDocId);

     Imcms.getServices().getDocumentService().save(Document saveMe);

     Imcms.getServices().getDocumentService().publishDocument(int docId, int userId);

     Imcms.getServices().getDocumentService().index(int docId);

     Imcms.getServices().getDocumentService().copy(int docId);

     Imcms.getServices().getDocumentService().deleteByDocId(Integer docIdToDelete);

     Imcms.getServices().getDocumentService().getUniqueAlias(String alias);

Description parameters
----------------------

+----------------------+--------------+--------------------------------------------------+
| Attribute            | Type         | Description                                      |
+======================+==============+==================================================+
| type                 | DocumentType | Imcms support any types (FILE, HTML,TEXT,URL)    |
+----------------------+--------------+--------------------------------------------------+


Description about Document
--------------------------

Document it is super class for ``TextDocumentDTO``, ``DocumentDTO``, ``UrlDocumentDTO``, ``FileDocumentDTO``.
Therefore, we can easily return any type of data that need - just inject call methods to above documents constructor;

Example
"""""""
.. code-block:: jsp

  TextDocumentDTO documentDTO = new TextDocumentDTO(Imcms.getServices().getDocumentService().createNewDocument(Meta.DocumentType.TEXT, 1001));




