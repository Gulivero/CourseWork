using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace HumanResourceServer.Models
{
    public partial class HumanResourceContext : DbContext
    {
        public HumanResourceContext()
        {
        }

        public HumanResourceContext(DbContextOptions<HumanResourceContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Departments> Departments { get; set; }
        public virtual DbSet<Workers> Workers { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. See http://go.microsoft.com/fwlink/?LinkId=723263 for guidance on storing connection strings.
                optionsBuilder.UseSqlServer("Server=(local);Database=HumanResource;user=Gulivero;password=12345678;");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Departments>(entity =>
            {
                entity.HasIndex(e => e.NameOfDepartment)
                    .HasName("UQ__Departme__010EE3FBFBA0FE2F")
                    .IsUnique();

                entity.Property(e => e.Id).HasColumnName("_id");

                entity.Property(e => e.NameOfDepartment)
                    .IsRequired()
                    .HasColumnName("name_of_department")
                    .HasMaxLength(50);
            });

            modelBuilder.Entity<Workers>(entity =>
            {
                entity.HasIndex(e => e.Login)
                    .HasName("UQ__Workers__7838F27203649C42")
                    .IsUnique();

                entity.HasIndex(e => e.Password)
                    .HasName("UQ__Workers__6E2DBEDE387AD02E")
                    .IsUnique();

                entity.Property(e => e.Id).HasColumnName("_id");

                entity.Property(e => e.Department)
                    .IsRequired()
                    .HasColumnName("department")
                    .HasMaxLength(50);

                entity.Property(e => e.Login)
                    .IsRequired()
                    .HasColumnName("login")
                    .HasMaxLength(50);

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasColumnName("name")
                    .HasMaxLength(50);

                entity.Property(e => e.Password)
                    .IsRequired()
                    .HasColumnName("password")
                    .HasMaxLength(50);

                entity.Property(e => e.Position)
                    .IsRequired()
                    .HasColumnName("position")
                    .HasMaxLength(50);

                entity.Property(e => e.Salt)
                    .HasColumnName("salt")
                    .HasMaxLength(250)
                    .IsUnicode(false);

                entity.Property(e => e.Sex)
                    .IsRequired()
                    .HasColumnName("sex")
                    .HasMaxLength(50);

                entity.Property(e => e.Surname)
                    .IsRequired()
                    .HasColumnName("surname")
                    .HasMaxLength(50);

                entity.HasOne(d => d.DepartmentNavigation)
                    .WithMany(p => p.Workers)
                    .HasPrincipalKey(p => p.NameOfDepartment)
                    .HasForeignKey(d => d.Department)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK__Workers__salt__1920BF5C");
            });
        }
    }
}
