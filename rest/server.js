const express = require('express');
const fs = require('fs');
const app = express();
const port = 3000;

app.use(express.json());

let db = require('./db.json');
let posts = db.posts;

app.get('/posts', (req, res) => {
  res.json({ posts: posts });
});

app.get('/posts/:id', (req, res) => {
    
  const postId = parseInt(req.params.id);
  console.log(`GET /posts/${postId}`)
  const post = posts.find(p => p.id === postId);

  if (post) {
    res.json({ posts: [post] });
  } else {
    res.status(404).json({ error: 'Nie znaleziono posta' });
  }
});

app.get('/posts/:id/comments', (req, res) => {
  
  const postId = parseInt(req.params.id);
  console.log(`GET /posts/${postId}/comments`);
  const post = posts.find(p => p.id === postId);

  if (post) {
    res.json({ comments: post.comments });
  } else {
    res.status(404).json({ error: 'Nie znaleziono komentarzy' });
  }
});
app.put('/posts/:id/comments', (req, res) => {  
    const postId = parseInt(req.params.id);
    console.log(`PUT /posts/${postId}/comments`);
    const postIndex = posts.findIndex(p => p.id === postId);
  
    if (postIndex !== -1) {
      const newComments = req.body;
        console.log(newComments);
      posts[postIndex].comments = newComments;
  
      db.posts = posts;
      fs.writeFile('./db.json', JSON.stringify(db, null, '\t'), (err) => {
        if (err) {
          console.error(err);
          res.status(500).json({ error: 'Błąd serwera' });
        } else {
          res.json({ success: true });
        }
      });
    } else {
      res.status(404).json({ error: 'Nie znaleziono posta' });
    }
  });

app.listen(port, () => {
  console.log(`Serwer działa na porcie: http://localhost:${port}`);
});